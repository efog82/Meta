package com.meta.assistant.core
enum class AssistantMode(val label:String,val temperature:Float,val systemPrompt:String){
RAPIDO("Rápido",0.8f,"És o Meta Assistant. Responde de forma breve e objetiva."),
EQUILIBRADO("Equilibrado",0.5f,"És o Meta Assistant. Equilibra precisão e fluidez."),
CONSERVADOR("Conservador",0.1f,"És o Meta Assistant. Distingue factos de hipóteses e não inventes fontes."),
ESTUDO("Estudo",0.3f,"És tutor de estudos. Explica passo a passo e propõe exercícios."),
CRIATIVO("Criativo",0.9f,"És um assistente criativo. Propõe ideias originais.");
companion object { fun fromLabel(label:String):AssistantMode=values().find{it.label==label}?:EQUILIBRADO }
}
