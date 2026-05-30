# Reparador de áudio para macOS.
Se isto ajudar pelo menos uma pessoa, estarei satisfeito.

No meu caso, o macbook conseguia emitir som somente uma vez e após breve inatividade ficava "mudo" e parava de reproduzir sons, mas voltava a funcionar se eu abaixasse a tampa e levantasse novamente ou reiniciasse o sistema.
Após reinstalar o macOS em várias versões e não resolver a situação, então parece ser problema de hardware.
Tive a ideia de criar um aplicativo para ficar "reproduzindo" infinitamente um arquivo de som vazio, mantendo assim o serviço de áudio sempre em atividade, então agora o macbook funciona normalmente a reprodução de áudio. 

Abaixo estão as instruções de como gerar os "executáveis" no macOS.

## Instalar o brew (https://brew.sh):
````
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
````

## Instalar o jpackage (https://dev.java/learn/jvm/tool/jpackage/):
````
brew install jpackage
````

## Gerar arquivo JAR no intelliJ:
No lado direito abra a aba Maven, expanda o item "Lifecycle" e dê clique duplo em "package".

## Gerar arquivo APP via terminal:
#### Entrar na pasta target e rodar o comando abaixo.
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.RepararAudioApp \
--main-jar RepararAudioApp-1.2.jar \
--type app-image \
--icon classes/RepararAudioApp.icns
````

## Gerar arquivo DMG via terminal:
#### Entrar na pasta target e rodar o comando abaixo.
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.RepararAudioApp \
--main-jar RepararAudioApp-1.2.jar \
--type dmg \
--icon classes/RepararAudioApp.icns
````