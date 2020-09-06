# jbsn

A school notes manager for macOS written in Java using pages automation (currently working on de-crappy-fying the code)
![Main window](https://github.com/edgelord314/jbsn/blob/master/screenshots/GreetingWindow.png)

### Features

- Create notes with a given headline and subject and the program will automatically create and open a new pages document with a template for your notes

- Get an overview of all notes taken, or filter your notes using powerful filters

- Get a list of relevant notes for the next school day

- Add timestamps after a class test, to see everything important for the next class test (wip)

- Create and see an overview of each school day to increase productivity and grades



### Installation

If there is a release to download, you only need a jre, macOS, pages and a pages template called SchoolNote (further information below)

1. Requirements: jdk 1.8 or higher, macOS, pages and a pages template called SchoolNote (see below for further information), git and maven

2. clone the repository and build the project using maven

3. Run the jar a first time to install the software and any additional time to actually run it



### The pages template

jbsn creates new notes from a pages template called `SchoolNote`, which you have to create and save yourself. To do this, open up pages and create a layout for your notes. You can include placeholder elements `SUBJECT`, `DATE` and `HEADLINE` (write the keywords where you want them to be and with the formatting you want, select one of them and go to Format > Advanced > Define as placeholder text). When you're done, go to File > Save as Template... and give the template the name `SchoolNote`. You can then exit pages without saving the document.
