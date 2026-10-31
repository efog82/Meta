# Meta Assistant (Android)

Projeto independente do Atlas. Inclui quatro separadores: Chat, Agenda, Notas e Estudo; cinco modos de resposta; base de dados local Room; arquitetura de motor híbrido.

## Estado atual
- Agenda e notas: permitem adicionar e consultar dados locais.
- Chat: interface e histórico local funcionais.
- IA offline: **ainda não integrada**. A dependência MediaPipe está incluída, mas não foi implementado o carregamento de um modelo.
- IA online: **ainda não integrada**. Não há fornecedor nem credenciais configurados.
- Estudo: interface preparada, sem geração de resumos.
- A aplicação não simula respostas de IA quando não existe modelo configurado.

## Compilar no GitHub
Actions > Build APK > Run workflow. O APK de depuração, se a compilação terminar com sucesso, fica nos artefactos da execução.

## Segurança e limitações
Não incluir tokens no repositório. A base Room não está encriptada; backups estão desativados. As migrações futuras terão de ser explicitamente implementadas. Não foi executada uma compilação local antes desta publicação.
