# Reparador de áudio para macOS.
Se isto ajudar pelo menos uma pessoa, estarei satisfeito.

No meu caso, o macOS conseguia emitir som somente 1 vez e após breve inatividade ficava "mudo" e parava reproduzir sons, talvez por falha de hardware, mas voltava a funcionar se eu abaixasse a tela e levantasse novamente ou reiniciando o sistema.
Tive a ideia de criar um aplicativo para ficar "reproduzindo" infinitamente um arquivo de som vazio, mantendo assim o serviço de áudio sempre em ativiade.

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
#### No lado direito:
Abra a aba Maven
#### Expanda:
Lifecycle
#### Dê duplo clique em:
package

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