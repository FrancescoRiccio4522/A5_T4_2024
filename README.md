# Task 4 - Gruppo A3
Componenti:
- Valerio Domenico Conte - M63001606
- Nike Di Giacomo - M63001641
- Alessandro Falino - M63001658

# GUIDA ALL'INSTALLAZIONE

## PASSO 1
Scaricare Docker Desktop per il proprio sistema operativo.

## PASSO 2
Si deve avviare lo script "installer.bat" se si sta usando una distribuzione Windows oppure "installermac.sh" nel caso si utilizzi macOS o una distro di Linux.
Per MacOS - eseguire nella cartella dove è presente il file ”installermac.sh” il comando "chmod +x installermac.sh" per renderlo eseguibile, e poi "./installermac.sh" per eseguirlo.
Tali script dovranno essere avviati unicamnete con Docker in esecuzione, altrimenti l'installazione non partirà. Saranno effettuate le seguenti operazioni:
1) Creazione della rete "global-network" comune a tutti i container.
2) Creazione del volume "VolumeT9" comune ai Task 1 e 9 e del volume "VolumeT8" comune ai Task 1 e 8.
3) Creazione dei singoli container in Docker desktop.

NOTA: il container relativo al Task 9 ("Progetto-SAD-G19-master") si sospenderà autonomamente dopo l'avvio. Esso viene utilizzato solo per "popolare" il volume "VolumeT9" condiviso con il Task 1.

## PASSO 3
Si deve configurare il container "manvsclass-mongo_db-1" così come descritto anche nella documentazione del Task 1.
Per fare ciò bisogna fare le seguenti operazioni:
1) Posizionarsi all'interno del terminale del container
2) Digitare il comando "mongosh"
3) Digitare i seguenti comandi:

        use manvsclass
        db.createCollection("ClassUT");
        db.createCollection("interaction");
        db.createCollection("Admin");
        db.createCollection("Operation");
        db.ClassUT.createIndex({ difficulty: 1 })
        db.Interaction.createIndex({ name: "text", type: 1 })
        db.interaction.createIndex({ name: "text" })
        db.Admin.createIndex({username: 1})

L'intera applicazione è adesso pienamente configurata e raggiungibile sulla porta :80. Per una guida all'installazione e all'utilizzo più completa consultare la documentazione al capitolo 5.

# VIDEO DIMOSTRAZIONE
## Admin



https://github.com/Testing-Game-SAD-2023/T11-G41/assets/128593973/e4e1ef4d-f2f3-42b2-8be4-ef9caa219b93



## Player



https://github.com/Testing-Game-SAD-2023/T11-G41/assets/128593973/c94398b2-178e-4b7f-a6d6-685c2689c03c

