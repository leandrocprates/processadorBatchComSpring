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
public class Dados {
    private String who;
    private String time;
    private String message;
}
