# FEAL-4 - Linear Cryptanalysis using 200 known plaintext

The code in this repository uses a CSV file with 200 pairs plaintext-ciphertext to discover the keys used during the encryption.

It takes about a minute to complete and will be able to recover the follwoing bits:
* 28 bits for K0, 
* 27 bits for K1, 
* 28 bits for K2, 
* 27 bits for K3, 
* 30 bits for K4, and
* 30 bits for K5.

> To launch the process, use the test class "AnalysisTest". 

The math formulae used are described in extenso in the [report](docs/Report.pdf).

Mark: 24/25

NB: For differential cryptanalysis, you may want to visit Twice 22's Repo [here](https://github.com/Twice22/feal-4-differential-cryptanalysis).

Disclaimer: this was done for academic purposes. Don't try and reproduce it at home, kids 😉️.

