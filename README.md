# Progetto MOVIDA (Corso di Algoritmi e Strutture Dati 2019/2020)

Componenti gruppo:
- Angelo Galavotti: 0000924743
- Letizia Gorini: 0000924219
	
Algoritmi di ordinamento implementate:
- Insertion Sort
- Quick Sort
	
Strutture dati implementate:
- HashTable a indirizzamento aperto
- Lista non ordinata.
	
---------------------------------------
## Istruzioni di Inizializzazione

Per inizializzare il programma, creare un istanza di tipo MovidaCore (specificando la struttura dati e l'algoritmo di ordinamento nel 
costruttore). Specificare il file da cui caricare i dati, e chiamare la funzione loadFromFile, inserendolo come parametro.

---------------------------------------
## Note sull'implementazione

- Map:
	La classe contiene delle funzioni astratte (ovvero che richiedono implementazione) che rispecchiano i metodi che si trovano solitamente
	nelle strutture dati di tipo dizionario. Elem rappresenta un record di una delle due strutture.
	
- HashMap: 
	Questa struttura dati permette di istanziare hashMap a indirizzamento aperto con diverse funzioni di hashing. In movidaCore (e in generale per
	chiavi di tipo stringa) si fa uso della funzione hashCode di Java. Oltre a ciò, se non si specifica la dimensione dell'hashTable al
	momento dell'istanziamento, la tabella viene ridimensionata in modo automatico.
	
- UnorderedLinkedList: 
	La classe implementa una lista bidirezionale non ordinata, e contiene il riferimento al primo elemento della lista (ovvero il campo root).
	Oltre all'inserimento in testa, è possibile sfruttare una funzione di inserimento in coda.

- MovidaGraph:
	La classe contiene l'implementazione di un grafo attraverso una HashMap avente per chiave un oggetto di tipo 'Persona' e per valore 
	un ArrayList contentente tutte le collaborazioni che interessano quell'attore. Oltre a ciò, in MovidaGraph c'è anche la classe PrimDistElem,
	che associa a un nodo (appunto, l'oggetto di tipo persona) con il punteggio massimo associato (ovvero la distanza massima del nodo dalla source).
	Questa classe in particolare viene usata nelle Coda Priorità per l'algoritmo di Prim, a sua volta chiamato in maiximiseCollaboration.

- MovidaCore:
	Nella classe MovidaCore abbiamo 3 strutture dati, una per i film, una per le persone e una per le collaborazioni.
	Mentre per le strutture dei film e delle persone si può scegliere fra HashTable e UnorderedLinkedList, la struttura delle collaborazioni
	è limitata a MovidaGraph.
	In MovidaCore inoltre abbiamo la classe MovieCount, che associa a un attore il numero di film a cui ha partecipato. Anche qui, la sua esistenza
	è dovuta all'uso delle Code Priorità in searchMostActiveActors().
	Dopo che si è specificato il file in loadFromFile, questo viene cachato in una variabile di tipo File, in modo da permette di ricaricare
	i dati nelle varie strutture chiamando reload().

- Sort:
	La classe Sort contiene le funzioni degli algoritmi di ordinamento (nel nostro caso InsertionSort e QuickSort) ed alcune classi statiche
	che permettono di filtrare l'ordinamento in base agli attributi della classe Movie (quindi titolo, anno, voti etc).

---------------------------------------
## Note sulle funzioni

- MovidaCore:
	- setMap(), oltre che a riassegnare il tipo di strutture dati, ricarica anche tutti i dati nella nuova struttura.
	- reload(), ricarica i dati nelle strutture della classe.
	- rmvWhiteSpaces(String) permette di rimuovere gli spazi in eccesso da una stringa, sia in mezzo a due parole separate, che alle estremità della 
	stringa stessa.
	
- UnorderedLinkedList e HashMap:
	- Queste classi contengono funzioni che nonostante non vengano utilizzate per gli scopi del progetto, sono state comunque incluse per 
	rendere l'implentazione più completa e generale.
	
---------------------------------------
## Note sulle eccezioni

Le classi delle eccezioni si trovano nella cartella movida.exceptions. Segnalano errori di HashTable Overflow (se si fa uso di tabelle hash
senza ridimensionamento automatico) e di UnknownMap e UnknownSort in caso si inizializzasse un oggetto di tipo MovidaCore facendo uso di
algoritmi o strutture non supportate.
