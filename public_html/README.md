# Mensageiro com RMI e Socket
Trabalho realizado para a disciplina de Sistemas Distribuídos - UERJ - 2017.2

## Arquitetura
A arquitetura se baseia na troca de mensagens RMI entre cliente e servidor e na troca de mensagens entre servidores com multicast, utilizando sockets UDP. Ela foi escolhida devido à sua simplicidade. Nesta arquitetura, apenas é necessário transmitir um único datagrama para o grupo a cada mensagem. Além disso, como o sistema executa em uma rede local, a probabilidade de pacotes serem perdidos é muito pequena.

![Arquitetura Multicast](https://i.imgur.com/IMH1Tx6.png)

### Servidor
Conforme pode ser visto na imagem, o servidor é composto de duas partes: uma socket UDP e outra RMI. A parte socket é responsável por lidar com o envio e o recebimento de mensagens via multicast, conectando-se com outros servidores do mesmo grupo. Já a parte RMI conecta-se aos clientes deste servidor, tendo seu serviço registrado no RMI Registry.

### Clientes
Cada cliente do sistema é similar à parte RMI do servidor. Seu serviço é registrado no RMI Registry, para que a parte cliente do servidor consiga acessá-lo. 

## Executando
- Executar o script `start-server.sh`:
	- O RMI Registry será iniciado em localhost:9915.
	- O código fonte do sistema (arquivos .java) será compilado (em arquivos .class).
	- O servidor da aplicação será iniciado. Ao executar, o servidor exibe no terminal todas as mensagens, eventos de entrada e eventos de saída de clientes do sistema de mensagens conforme estes forem recebidos.
- Executar o script `start-client.sh`:
	-  O cliente da aplicação será iniciado. Neste momento, será solicitado que o nome deste cliente seja digitado.
	- Após o nome do cliente ser inserido, todos os clientes ativos receberão uma mensagem avisando que este cliente entrou na conversa.
	- Para enviar uma mensagem, basta digitá-la e pressionar a tecla "Enter".
	- Novas mensagens recebidas do servidor são automaticamente exibidas no terminal.
	- Para sair da conversa, basta enviar a mensagem "sair". Todos os outros clientes ativos receberão uma mensagem indicando que este cliente saiu da conversa.
	

![Servidor](https://i.imgur.com/w9VrT22.png)

![Cliente 1 - Cachorro](https://i.imgur.com/H6Uj3Zq.png)

![Cliente 2 - Gato](https://i.imgur.com/LHl7hL3.png)

![Cliente 3 - Boi](https://i.imgur.com/qkoQqEZ.png)

## Funcionamento
### Mensagem
Uma mensagem possui o nome do cliente que a enviou, o instante em que ela foi enviada e o texto que está sendo transmitido.

### Servidor
- Ao iniciar, carrega as configurações do sistema do arquivo config.properties.
- Registra seu serviço no RMI Registry com o nome carregado do arquivo de configurações.
	- Este serviço envia mensagens via multicast para o IP e porta indicados no arquivo de configurações.
- Entra no grupo multicast e aguarda por novas mensagens vindas de outros servidores.
	- Quando uma mensagem é recebida via multicast, uma consulta ao RMI Registry é realizada, para que seja obtida a lista atual de serviços de clientes ativos.
	- A mensagem é enviada a todos os clientes deste servidor, presentes na lista, chamando o serviço de cada um deles. 

### Cliente
- Ao iniciar, carrega as configurações do sistema do arquivo config.properties.
- Solicita o nome do cliente para o usuário.
- Registra seu serviço no RMI Registry com o nome carregado do arquivo de configurações associado ao nome do cliente obtido no passo anterior.
	- Este serviço imprime no terminal todas as mensagens recebidas.
- Utiliza o serviço disponibilizado pelo servidor para avisar a todos que entrou na conversa.
- Aguarda o usuário digitar e enviar mensagens.
	- Se a mensagem enviada for "sair": utiliza o serviço disponibilizado pelo servidor para avisar a todos que saiu da conversa, remove o registro do seu serviço no RMI Registry e finaliza este processo.
	- Se qualquer outra mensagem for enviada: utiliza o serviço disponibilizado pelo servidor para enviar a mensagem a todos os participantes da conversa.