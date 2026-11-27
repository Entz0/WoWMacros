# WoWMacros – Visuell Macro-byggare

En native Android-app byggd i Kotlin med Jetpack Compose och Material 3 för att skapa, hantera och hålla koll på World of Warcraft-makron.

## Funktioner
- Lägg till makron med namn, kategori och makrokod (tom text kan inte sparas).
- Teckenräknare i realtid mot WoW:s gräns på 255 tecken samt snabbknapp för `@mouseover`.
- Bocka av makron som testade i spelet, redigera enskilda rader i en dialog och ta bort valda rader.
- Statistikrad (`X av Y makron testade`) och hjälptext när listan är tom.
- Uppbyggd med `ViewModel`, `StateFlow` och en samlad `MacroUiState`.

## Felsökning
När jag byggde redigeringsdialogen (`EditMacroDialog`) märkte jag ett problem i appens beteende där textfälten kunde hålla kvar gammal text från ett tidigare redigerat makro om man öppnade redigering för en annan rad. Det berodde på att `remember` behöll det gamla lokala tillståndet i dialogen. Jag löste problemet genom att skicka med makrots unika ID som nyckel (`remember(macro.id)`), så att fälten alltid läser in rätt makros namn, kategori och kod när en ny rad väljs.

## Hjälpmedel
Under projektet använde jag främst studieguiden, Android Studios egna felmeddelanden och AI som stöd. Jag använde AI för att bryta ner uppgiften i mindre steg och få förklaringar på begrepp jag var osäker på, till exempel `@Composable`, `remember`, `map` och `copy`. Det som hjälpte mig mest var att testa varje ändring direkt i emulatorn och be om korta förklaringar rad för rad istället för att bara klistra in kod, då fastnade det hur `ViewModel` och `UiState` faktiskt hänger ihop.