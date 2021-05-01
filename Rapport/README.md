Pour compiler la source tex il faut:
- le package "pdfescape"
- l' option de compilation "--shell-esacpe" 
- Python 3 avec la package "Pygments" (https://pypi.org/project/Pygments/)

La compilation du document a été testé sur un Windows 10 64bits avec la dernière version de MiKTeX via pdflatex:

pdflatex.exe -synctex=1 -interaction=nonstopmode --shell-escape "SUPER DEMINEUR".tex