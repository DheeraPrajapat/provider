package com.marius.valeyou_admin.data.beans;

public class Aarin {
    String nome;
    String agencia="";
    String numeroConta;
    String numeroDocumento="";
    String ispb;
    String chave="";
    String tipoChave;

    public Aarin(String nome, String agencia, String numeroConta, String numeroDocumento, String ispb, String chave, String tipoChave) {
        this.nome = nome;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.numeroDocumento = numeroDocumento;
        this.ispb = ispb;
        this.chave = chave;
        this.tipoChave = tipoChave;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getIspb() {
        return ispb;
    }

    public void setIspb(String ispb) {
        this.ispb = ispb;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(String tipoChave) {
        this.tipoChave = tipoChave;
    }


}