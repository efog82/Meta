package com.meta.assistant.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.meta.assistant.MetaApp
import com.meta.assistant.R
import com.meta.assistant.core.AssistantMode
import com.meta.assistant.core.HybridEngine
import com.meta.assistant.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
class ChatFragment:Fragment(){
private var mode=AssistantMode.EQUILIBRADO
private var useOnline=false
private var generation:Job?=null
override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?):View{
val root=inflater.inflate(R.layout.fragment_chat,container,false)
val group=root.findViewById<ChipGroup>(R.id.chipMode)
val online=root.findViewById<SwitchMaterial>(R.id.switchOnline)
val log=root.findViewById<TextView>(R.id.chatLog)
val input=root.findViewById<EditText>(R.id.inputMessage)
val send=root.findViewById<Button>(R.id.btnSend)
val stop=root.findViewById<Button>(R.id.btnStop)
group.setOnCheckedStateChangeListener{_,ids->val id=ids.firstOrNull()?:return@setOnCheckedStateChangeListener;mode=AssistantMode.fromLabel(root.findViewById<Chip>(id).text.toString())}
online.setOnCheckedChangeListener{_,enabled->useOnline=enabled}
val db=(requireActivity().application as MetaApp).db
viewLifecycleOwner.lifecycleScope.launch{log.text=db.chatDao().getAll().joinToString("\n\n"){"${it.role}: ${it.content}"}}
stop.setOnClickListener{generation?.cancel();generation=null;send.isEnabled=true}
send.setOnClickListener{
val prompt=input.text.toString().trim()
if(prompt.isEmpty()||generation?.isActive==true)return@setOnClickListener
input.text.clear();send.isEnabled=false
log.append("\n\nTu: $prompt\nAssistente: ")
generation=viewLifecycleOwner.lifecycleScope.launch{
try{
db.chatDao().insert(ChatMessage(role="user",content=prompt,mode=mode.label))
val out=StringBuilder()
HybridEngine(db).generateResponse(prompt,mode,useOnline).collect{token->out.append(token);log.append(token)}
db.chatDao().insert(ChatMessage(role="assistant",content=out.toString(),mode=mode.label))
}finally{send.isEnabled=true}
}
}
return root
}
override fun onDestroyView(){generation?.cancel();generation=null;super.onDestroyView()}
}
class AgendaFragment:Fragment(){
override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?):View{
val root=inflater.inflate(R.layout.fragment_list,container,false)
root.findViewById<TextView>(R.id.listTitle).text="Agenda"
val input=root.findViewById<EditText>(R.id.listInput)
input.hint="Nova tarefa..."
val list=root.findViewById<TextView>(R.id.listContent)
val db=(requireActivity().application as MetaApp).db
fun refresh(){viewLifecycleOwner.lifecycleScope.launch{list.text=db.agendaDao().getAll().joinToString("\n"){(if(it.done)"[x] " else "[ ] ")+it.title}}}
root.findViewById<Button>(R.id.btnAdd).setOnClickListener{
val title=input.text.toString().trim()
if(title.isNotEmpty())viewLifecycleOwner.lifecycleScope.launch{db.agendaDao().insert(AgendaItem(title=title));input.text.clear();refresh()}
}
refresh();return root
}
}
class NotesFragment:Fragment(){
override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?):View{
val root=inflater.inflate(R.layout.fragment_list,container,false)
root.findViewById<TextView>(R.id.listTitle).text="Notas"
val input=root.findViewById<EditText>(R.id.listInput)
input.hint="Escreve uma nota..."
val list=root.findViewById<TextView>(R.id.listContent)
val db=(requireActivity().application as MetaApp).db
fun refresh(){viewLifecycleOwner.lifecycleScope.launch{list.text=db.noteDao().getAll().joinToString("\n\n"){it.text}}}
root.findViewById<Button>(R.id.btnAdd).setOnClickListener{
val value=input.text.toString().trim()
if(value.isNotEmpty())viewLifecycleOwner.lifecycleScope.launch{db.noteDao().insert(Note(text=value));input.text.clear();refresh()}
}
refresh();return root
}
}
class StudyFragment:Fragment(){
override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?):View{
val root=inflater.inflate(R.layout.fragment_list,container,false)
root.findViewById<TextView>(R.id.listTitle).text="Estudo"
root.findViewById<EditText>(R.id.listInput).hint="Tópico para estudar"
root.findViewById<Button>(R.id.btnAdd).setOnClickListener{Toast.makeText(requireContext(),"Gerador de resumos requer modelo de IA configurado.",Toast.LENGTH_LONG).show()}
return root
}
}
