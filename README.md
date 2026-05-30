# Reparador de áudio para macOS.
Se eu conseguir ajudar pelo menos uma pessoa, estarei satisfeito.
No meu caso, o macOS consegue emitir som mas somente 1 vez e após inatividade fica "mudo" e para de sair som, talvez por falha de hardware.
Tive a ideia de criar um aplicativo para ficar "reproduzindo" infinitamente um arquivo de som vazio, mantendo assim o serviço de áudio sempre em ativiade.

Como gerar os executáveis no macOS:

Instalar o brew e o jpackage antes!

## JAR
#### No lado direito:
Abra a aba Maven
#### Expanda:
Lifecycle
#### Dê duplo clique em:
package

## APP
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.AudioTrayApp \
--main-jar RepararAudioApp-1.1.jar \
--type app-image \
--icon RepararAudioApp.icns
````

## DMG
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.AudioTrayApp \
--main-jar RepararAudioApp-1.1.jar \
--type dmg \
--icon RepararAudioApp.icns
````