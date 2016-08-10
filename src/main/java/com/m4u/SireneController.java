package com.m4u;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.*;
import java.nio.ByteBuffer;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DCN on 20/07/16.
 */
@RequestMapping("/siren")
@RestController
public class SireneController {

    private SerialPort port;

    public SireneController() throws Exception {
        System.out.println("Constructor");
        port = openArduinoPort();
    }

    @RequestMapping(path = "/on")
    public HttpEntity on() throws Exception{
        sendData((byte) 1);

        return new HttpEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/off")
    public HttpEntity off() throws Exception{
        sendData((byte) 0);

        return new HttpEntity(HttpStatus.OK);
    }

    private SerialPort openArduinoPort() throws Exception{
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/cu.usbserial-AH00PEMK");
        if ( portIdentifier.isCurrentlyOwned() )
        {
            throw new RuntimeException("Error: Port is currently in use");
        }

        CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

        if ( commPort instanceof SerialPort ) {
            SerialPort serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            return serialPort;
        }

        throw new RuntimeException("Error: Port is not SerialPort");
    }

    private void sendData ( byte b ) throws Exception
    {
        OutputStream out = port.getOutputStream();
        out.write(b);
        out.close();
    }

}