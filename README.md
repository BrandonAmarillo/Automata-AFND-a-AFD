# Automata-AFND-a-AFD

Este proyecto resuelve el Trabajo Práctico Integrador de Fundamentos de Informática, permitiendo la conversión de autómatas finitos no deterministas (AFND) a autómatas finitos deterministas (AFD). Además, permite Minimizar los AFD. El programa está desarrollado en Java y utiliza Maven para la gestión de dependencias y compilación.

## Requisitos

- **Java 21** o superior
- **Maven 3.6** o superior

### Instalación de Java y Maven

#### En Linux (Debian/Ubuntu)

```sh
sudo apt update
sudo apt install openjdk-21-jdk maven
```
### En Windows
- Descargar e instalar [java 21 JDK](https://www.oracle.com/java/technologies/downloads/).
- Descargar e instalar [Maven](https://maven.apache.org/download.cgi).

Para verificar la instalación, ejecute en la terminal o consola:
```sh
java -version
mvn -version
```
### Compilación y ejecución
Desde la raíz del proyecto, ejecutar:
```sh
mvn clean compile o mvn clean packege
```
Esto genera un archivo ```.jar``` en la carpeta ```target/```.</br>
Para ejecutar el programa:
```sh
mvn exc:java
```
o directamente
```sh
java -cp target/automatas-1.0-SNAPSHOT.jar com.unpsjb.automatas.application.Main
```

### Dependencias
El proyecto utiliza las siguientes librerías:
- [Gson v 2.11.9](https://github.com/google/gson) para manejo de JSON.
- [iText 7 v 7.2.5](https://itextpdf.com/) para generar PDFs.
Estas dependencias se gestionan automáticamente con **Maven**.
</br>
Nota: Si usan VScode deben tener instalado ```Extension Pack for Java```.