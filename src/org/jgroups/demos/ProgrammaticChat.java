package org.jgroups.demos;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.ProtocolStack;
import org.jgroups.util.Util;

import java.net.InetAddress;

/**
 * @author Bela Ban
 * @version $Id: ProgrammaticChat.java,v 1.1 2010/10/06 09:46:31 belaban Exp $
 */
public class ProgrammaticChat {

    public static void main(String[] args) throws Exception {
        JChannel ch=new JChannel(false);
        ProtocolStack stack=ch.createProtocolStack();
        stack.addProtocol(new UDP().setValue("bind_addr", InetAddress.getByName("192.168.1.5"))).
                addProtocol(new PING()).addProtocol(new MERGE2()).addProtocol(new FD_SOCK()).
                addProtocol(new FD_ALL()).addProtocol(new VERIFY_SUSPECT()).addProtocol(new BARRIER()).
                addProtocol(new NAKACK()).addProtocol(new UNICAST2()).addProtocol(new STABLE()).
                addProtocol(new GMS()).addProtocol(new UFC()).addProtocol(new MFC()).addProtocol(new FRAG2());
        stack.init();

        ch.setReceiver(new ReceiverAdapter() {
            public void viewAccepted(View new_view) {
                System.out.println("view: " + new_view);
            }

            public void receive(Message msg) {
                System.out.println("<< " + msg.getObject() + " [" + msg.getSrc() + "]");
            }
        });

        ch.connect("ChatCluster");


        for(;;) {
            String line=Util.readStringFromStdin(": ");
            ch.send(null, null, line);
        }
    }

}

