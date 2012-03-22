################################################################################
# Makefile
################################################################################

report_NAME	= rapport
bib_FILE		= bibliographie.bib
input_DIR 	= ./input
needed_FILES	= $(wildcard $(input_DIR)/*.tex) $(bib_FILE)

default: $(report_NAME).pdf

$(report_NAME).pdf: $(report_NAME).tex $(needed_FILES)
	pdflatex $<
	bibtex $(report_NAME)
	pdflatex $(report_NAME).tex
	pdflatex $(report_NAME).tex

################################################################################

.PHONY: clean mrproper

clean:
	rm -rf *.aux *.bbl *.blg *.log *.out *.toc

mrproper: clean
	rm -rf $(report_NAME).pdf

