# Reparador de áudio para macOS.
Se eu conseguir ajudar pelo menos alguma pessoa além de mim, estarei satisfeito.
No meu caso, o macOS consegue emitir som somente 1 vez e por inatividade, talvez por falha de hardware, para de sair som.
Tive a ideia de colocar um aplicativo para ficar "tocando" infinitamente um arquivo de som vazio, mantendo assim o serviço de áudio sempre em ativiade.

Como gerar os executáveis no macOS:

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
--main-jar app.jar \
--type app-image
````

## DMG
````
jpackage \
--name RepararAudioApp \
--input . \
--main-class br.com.ersys.AudioTrayApp \
--main-jar app.jar \
--type dmg
````