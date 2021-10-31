# Java Flugsimulator

Der Java Flugsimulator ist ein Modell zum spielerischen Erlernen
der Programmierung. Dabei nimmt ein Flugzeug die Rolle des klassischen
Computers ein. Dem Flugzeug können Befehle erteilt werden, die
dieses dann ausführt.

Mithilfe dieser Befehle können Sie als Programmierer bestimmte
Aufgaben lösen. Zum Lösen der Aufgaben wird eine Programmiersprache,
welche fast vollständig der Programmiersprache Java entspricht,
verwendet.

## Landschaft
Die Welt des Flugsimulators wird durch eine gekachelte Ebene
dargestellt. Die Größe der Ebene ist dabei nicht explizit vorgegeben.
Die Landschaft ist beliebig, aber nie unendlich, groß.

Auf einzelnen Kacheln können sich Passagiere befinden, die das
Flugzeug aufnehmen kann. Außerdem ist es möglich, dass auf
gewissen Kacheln ein Gewitter vorherrscht. Diese Kacheln können
von dem Flugzeug nicht betreten werden. Es ist dabei auch nicht
möglich, dass sich auf einer Kachel sowohl ein Passagier als auch
ein Gewitter befindet. Das Flugzeug kann das Territorium außerdem
nicht verlassen. Man kann sich dementsprechend vorstellen,
dass das Territorium immer von einem großen Gewittersturm umgeben ist,
den das Flugzeug nicht durchqueren kann.

## Flugzeug
Im Java Flugsimulator existiert immer genau ein Flugzeug. Das
Flugzeug steht dabei auf einer der Kacheln des Territoriums.
Diese Kachel darf keinen Gewittersturm haben, es können sich jedoch
Passagiere auf dieser befinden. Das Flugzeug kann in vier verschiedene
Blickrichtungen auf der Kachel sein (Norden, Osten, Süden, Westen).

Passagiere können sich dabei nicht nur auf Kacheln, sondern auch in
dem Flugzeug befinden.

## Befehle
Das Flugzeug kennt vier Befehle und drei Testbefehle, durch deren
Aufruf ein Programmierer das Flugzeug durch ein gegebenes Territorium
führen kann. Diese Befehle werden im Folgenden aufgelistet und in
der Zukunft in darauffolgenden Abschnitten noch näher spezifiziert:

- `void vor()`: Das Flugzeug bewegt sich eine Kachel in der aktuellen
Blickrichtung nach vorne.
- `void linksUm()`: Das Flugzeug bewegt sich auf der Kachel, auf der
es sich befindet, um 90 Grad gegen den Uhrzeigersinn.
- `void onboarden()`: Das Flugzeug nimmt einen Passagier von der
Kachel, auf der es sich befindet, auf, d. h. das Flugzeug hat einen 
Passagier mehr und die Kachel einen weniger.
- `void offboarden()`: Das Flugzeug lässt einen Passagier auf der
Kachel, auf der es sich befindet, raus, d. h. das Flugzeug hat einen
Passagier weniger und die Kachel einen mehr.
- `boolean vornFrei()`: Liefert den Wert *true*, wenn sich in der
Kachel in Blickrichtung vor dem Flugzeug kein Gewitter befindet.
Befindet sich dort ein Gewitter, wird der Wert *false* geliefert.
- `boolean keinePassagiere()`: Liefert den Wert *true*, wenn sich
in dem Flugzeug keine Passagiere befinden. Befindet sich min. ein
Passagier im Flugzeug, wird der Wert *false* geliefert.
- `boolean passagierDa()`: Liefert den Wert *true*, falls sich
auf der Kachel, auf der sich das Flugzeug befindet, ein Passagier
befindet. Ist kein Passagier da, wird der Wert *false* geliefert.__
  
## Bemerkungen
In diesem Projekt wurden alle Aufgaben der einzelnen Aufgabenblätter
bearbeitet. In den einzelnen Klassen gibt es Kommentare sowie JavaDoc,
die den Code im Genaueren erklären.