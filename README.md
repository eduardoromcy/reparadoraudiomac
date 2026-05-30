# Reparador de áudio para macOS.
Se eu conseguir ajudar pelo menos uma pessoa, estarei satisfeito.
No meu caso, o macOS consegue emitir som somente 1 vez e após inatividade fica "mudo" e para de sair som, talvez por falha de hardware.
Tive a ideia de criar um aplicativo para ficar "reproduzindo" infinitamente um arquivo de som vazio, mantendo assim o serviço de áudio sempre em ativiade.

Como gerar os executáveis no macOS:

## Instalar o brew (https://brew.sh):
````
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
````

## Instalar o jpackage (https://dev.java/learn/jvm/tool/jpackage/):
````
brew install jpackage
````

## Gerar arquivo JAR
#### No lado direito:
Abra a aba Maven
#### Expanda:
Lifecycle
#### Dê duplo clique em:
package

## Gerar arquivo APP
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.RepararAudioApp \
--main-jar RepararAudioApp-1.2.jar \
--type app-image \
--icon classes/RepararAudioApp.icns
````

## Gerar arquivo DMG
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.RepararAudioApp \
--main-jar RepararAudioApp-1.2.jar \
--type dmg \
--icon classes/RepararAudioApp.icns
````