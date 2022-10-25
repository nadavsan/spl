package bgu.spl.mics;


import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl messageBus;

    @BeforeEach
    public void setUp(){
        messageBus.getInstance();
    }

    @Test
    void sendBroadcast() {
        MicroService HanSolo = new HanSoloMicroservice();
        MicroService C3PO = new C3POMicroservice();
        messageBus.register(HanSolo);
        messageBus.register(C3PO);
    }

//    @Test
//    void sendEvent() {
//        AttackEvent attackEvent = new AttackEvent();
//        messageBus.sendEvent(attackEvent);
//    }

//    @Test
//    void awaitMessage() {//throws InterruptedException {
//        HanSoloMicroservice HanSolo = new HanSoloMicroservice();
//        messageBus.register(HanSolo);
//        try {
//            messageBus.awaitMessage(HanSolo);
//        }
//    }
}