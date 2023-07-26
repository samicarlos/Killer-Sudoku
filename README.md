Jocul respecta urmatoarele reguli:\
\
-Fiecare linie, coloana si matrice de 3 pe 3 contine toate numerele de la 1 la 9 exact odata.\
-Suma tuturor numerelor dintr-un cage este egala cu numarul scris in coltul de stanga sus.\
-Nu exista numere care se repeta intr-un cage.\
\
Jocul este perfect functional, am rezolvat toate bug-urile pe care le-am intampinat. O chestie care ar putea fi observata este faptul ca in partea de jos a fiecarei celule este textfield-ul pentru notite care este setat sa nu poata fi editat daca nu este butonul "Notes" apasat, deci pentru a edita o celula trebuie apasat putin mai sus de partea de jos a acesteia. In rest, poate mai sunt chestii mici pe care nu le-am observat, dar programul functioneaza cum trebuie.\
\
Interfata: \
\
Aplicatia este realizata in JavaFx.\
Aceasta contine un layout de tip BorderPane cu urmatoarele:\
\
-pe partea de sus un HBox cu cateva butoane(acestea sunt butoane administrative adaugate doar cu scopul de a verifica si demonstra functionarea corecta a jocului)\
"Show Groups" arata valorile matricei care a fost folosita pentru calcularea sumei valorilor dintr-un cage\
"Show Colors" arata valorilor matricei care a fost folosita pentru generarea culorilor
(1-galben, 2-albastru, 3-verde, 4-rosu)\
"Show Player's Board" - arata valorile matricei pe care o modifica jucatorul in timp real
"Solve" arata valorile matricei care retine jocul rezolvat.\
"Clear" sterge valorile de pe ecran dar nu modifica nicio matrice\
Butonul de show player's board este cel care trebuie apasat pentru a continua jocul.\
\
-pe centru avem un GridPane cu 81 de grid-uri care reprezinta jocul in sine, pe fiecare grid avem un StackPane care contine urmatoarele:\
Textfield-ul principal pe care jucatorul il poate modifica doar cu valori intregi intre 1-9(valoarea unei celule din cele 81 a jocului sudoku); textfield-ul este setat sa nu mai poate fi editat daca este completat cu valoarea corecta\
Un label in colt sus stanga care reprezinta suma valorilor dintr-un cage;\
Un textfield in colt dreapta jos care reprezinta notitele jucatorului, acestea fiind validate tot doar cu valori intregi intre 1-9(notitele pot fi editate doar daca butonul de notes este activat(cand acesta este aprins), ceea ce dezactiveaza editarea textfield-ului principal; pentru a edita notitele trebuie apasat exact pe partea de jos a patratului)\
Liniile care impart cele 9 matrici generand astfel modelul sudoku sunt create prin editarea borderelor.\
\
-pe partea de jos un Hbox cu 3 butoane si un counter pentru greseli\
"New Game" creaza un joc nou\
"Notes" este un buton de toggle care permite editarea notitelor si dezactiveaza editarea valorilor jocului, acesta se aprinde cu o culoare albastra cand este activat.\
"Hint" rezolva un patrat la intamplare din jocul sudoku(jocul poate fi rezolvat apasand pe hint de 81 de ori)\
Counter-ul pentru greseli este incrementat in momentul in care jucatorul pune o valoare gresita intr-un patrat si pierde focusul acelui patrat.\
Greselile vor fi colorate cu rosu.\
La 4 greseli facute apare o fereastra noua pe care scrie "Game Over" si forteaza jucatorul sa inceapa alt joc.\
\
\
Modelul OOP:\
\
In clasa "Game" avem 4 matrici:\
solvedBoard reprezinta puzzle-ul sudoku care este generat la inceputul jocului\
playerBoard este matricea pe care o vede jucatorul\
coloredBoard - aceasta contine valori intre 1 si 4 care reprezinta culoarea patratului respectiv(1-galben, 2-albastru, 3-verde, 4-rosu); folosim aceasta matrice pentru a crea cage-urile specifice modului Killer Sudoku
groupsBoard - retine un "id" a fiecarui cage unic; folosim aceasta matrice pentru a calcula suma valorilor dintr-un cage\
\
\
GameLogic:\
\
Cum generam jocul:\
In functia generateBoard, generam valori random pana gasim 17 valori "corecte" ale jocului sudoku(17 celule diferite). La fiecare valoare generata verificam daca aceasta respecta regulile sudoku si daca puzzle-ul inca are o solutie posibila(chiar daca toate valorile sunt validate si respecta regulile jocului, acesta inca poate sa fie imposibil de completat). Daca verificarea dureaza prea mult(>300ms) atunci am intrat intr-o conditie foarte nefavorabila a functiei de rezolvare si resetam toate valorile generate pana acum si incercam din nou de la 0. Dupa ce am generat cele 17 celule folosim functia de rezolvare a jocului.\
\
Functia de validare a jocului:\
Verifica ca valoarea intrata sa nu se repete pe linie, coloana sau in matricea de 3 pe 3 din care face parte\
\
Functia de rezolvare a jocului:
Aceasta aplica o metoda brute-force de rezolvare a jocului, generand toate valorile posibile pana gaseste solutia corecta.\
\
Cum creem cage-urile specifice modului Killer Sudoku:\
Parcurgand matricea de culori pentru fiecare valoare nula(care nu contine inca nicio culoare) aplicam functia de alegere a culorii assignColor, apoi generam un numar(kMax) intre 2 si 4 care reprezinta dimensiunea cage-ului, apoi trimite aceste valori ca si parametri in functia recursiva addColor care adauga culoarea in matrice, decrementeaza valoarea lui kMax, genereaza o directie random(kNext: 1-sus, 2-dreapta, 3-jos, 4-stanga) si aplica din nou functia addColor pe vecinul din directia generata, daca respecta urmatoarele conditii: acesta nu are inca nicio culoare, nu strica dimensiunea cage-ului alipinduse cu alt cage de aceeasi culoare si nu are o valoare care a aparut deja pana acum in cage(numerele dintr-un cage nu se pot repeta); Functia se opreste cand kMax ajunge la 0.
Daca vecinul de pe directia generata in functia addColor nu respecta conditiile, atunci functia va returna valoarea lui kMax insemnand ca nu am reusit sa alipim nicio valoare in cage si avem un cage de marime 1. In acest caz vom verifica toti vecinii acelei valori din cage si vom colora primul vecin care respecta conditiile. Daca nici asa nu reusim sa marim cage-ul atunci vom incerca sa schimbam culoarea cage-ului respectiv si mai facem o ultima verificare a vecinilor cu tentativa de a colora primul vecin care respecta conditiile.\
\
assignColor - functia de alegere a culorii\
Aceasta verifica culorile celor 4 vecini si alege random o culoare care nu este deja folosita de vreun vecin de al sau.\
\
Cum calculam suma valorilor dintr-un cage:\
Parcurgand matricea de culori, folosim un counter care il crestem de fiecare data cand gasim o valoare in matricea groupsBoard care este 0. In momentul in care am gasit aplicam functia recursiva assingColorGroup peste aceasta care ii da valoarea lui counter("id-ul" sau) si aplica functia assignColorGroup in toate cele 4 directii, completand astfel intr-un mod dinamic tot cage-ul cu id-ul respectiv.\
Dupa ce am asignat corect toate "id-urile" cage-urilor putem calcula suma valorilor pentru fiecare "cage id" in parte.

