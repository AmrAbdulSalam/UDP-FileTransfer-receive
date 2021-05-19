/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment2part2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author Msys
 */
public class Threadser implements Runnable{
     private DatagramSocket socket;
    private boolean running;
    public static String packetMessage;
    public static DefaultListModel<String> listModel1;
    public static DefaultListModel<byte[]> listModel2;
    
    Threadser(){
        listModel1 = new DefaultListModel<>();
        listModel2 = new DefaultListModel<>();
    }
    
    public void createSocket(int port){
        try{
            socket = new DatagramSocket(port);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private String messageFromUser = "";
    @Override
    public void run() {
        System.out.println("inside run function");
        
        running = true;
        
        while(running){
            try{
                byte [] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data , data.length);
                socket.receive(packet);
                //data = packet.getData();
                String message = new String(packet.getData());
                if(message.contains("newPacket")){
                    String arr [] = message.split("::");
                    Client.listModel1.addElement(arr[0]);
                    Client.jList2.setModel(Client.listModel1);
                    while(true){
                        socket.receive(packet);
                        message = new String(packet.getData());
                        if(message.contains("endPacket"))
                            break;
                        else{
                            messageFromUser += message;
                        }
                    }
                }

                
                Client.listModel2.addElement(messageFromUser);
                //listModel2.addElement(messageFromUser);
                //Client.jList2.setModel(Client.listModel2);
                Client.statusText.setText("Recevied From IP = " + packet.getAddress() + " , Port = " + packet.getPort());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public void fun(){
        System.out.println("innnn" + Client.textEnter.getText());
        System.out.println("hi from : " + Client.messageSent);
    }
    public void start (){
        Thread thread = new Thread(this);
        thread.start();
    }
    public void stop (){
        running = false;
        socket.close();
    }
    private String st;
    public void sendTo(InetSocketAddress address , String mesg , byte[] byteFile){
        try{
            
            
            //System.out.println("st is : " + st);
            
            System.out.println("st is : " + st);
            listModel2.addElement(byteFile);
            //Client.jList2.setModel(listModel1);
            DatagramPacket packet = new DatagramPacket(byteFile , byteFile.length);
            packet.setSocketAddress(address);
            socket.send(packet);
            System.out.println("packet sent");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
