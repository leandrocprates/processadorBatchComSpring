/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.fcm;

import lombok.Data;

/**
 *
 * @author leandro.prates
 */
@Data
public class FcmData {
    private Dados data = new Dados(); 
    private String to;
    private String priority;
    private String restricted_package_name;
}