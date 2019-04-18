#include<Servo.h>
#include"default.h"
#ifndef _resource_h
#define _resource_h

buzzer buz(12);

nrDelay capClose(3000);
nrDelay capDetach(5000);
nrDelay capDuration(1000);
nrDelay capCloseDuration(50);



esp ep;

class usSensor{
private:
    const int trigPin,echoPin;
public:
    usSensor(int tpin,int epin):trigPin(tpin),echoPin(epin){
        pinMode(trigPin, OUTPUT);
        pinMode(echoPin, INPUT);
    }
    int getBinStatus(){
        long duration;
        int distance;
        digitalWrite(trigPin, LOW);
        delayMicroseconds(2);

        digitalWrite(trigPin, HIGH);
        delayMicroseconds(10);
        digitalWrite(trigPin, LOW);

        duration = pulseIn(echoPin, HIGH);

        distance= duration*0.034/2;

        //int b = (distance/30)*100;

        return distance ;//(b>=100?100:b);
    }
};

class Bin:public usSensor{
public:
    int binId,bins;
    Bin(int id,int s,int tpin,int epin):usSensor(tpin,epin),binId(id),bins(s){

    }
    int getbins(){
        updateStatus();
        return bins;
    }
    void updateStatus(){
        bins = usSensor::getBinStatus();
    }
    bool isFull(){
        if(bins <= 20){
                return true;
        }
        return false;
    }
};

Bin bin(1,0,6,7);

class IRSensor{
private:
    //int irPin,cTime,dTime;
	int irPin;
    int irSensorValue(){
        return analogRead(irPin);
        }
public:
    bool oflag,cflag;
    IRSensor(int irpin):irPin(irpin),oflag(true),cflag(true){

    }
    bool isObject(){
        //Serial.println(irSensorValue());
        if(irSensorValue()<=500)
            return true;
        return false;
    }
};

class Cap{
private:
    int in1,in2;
    bool sts;
    void cRotate(){
        digitalWrite(in1,HIGH);
        digitalWrite(in2,LOW);
    }
    void anRotate(){
        digitalWrite(in1,LOW);
        digitalWrite(in2,HIGH);
    }
public:
    Cap(int p1,int p2,bool s):in1(p1),in2(p2),sts(s){
        pinMode(in1,OUTPUT);
        pinMode(in2,OUTPUT);
    }
    void stop(){
        digitalWrite(in1,LOW);
        digitalWrite(in2,LOW);
    }
    void open(){
        if(sts==false){
            if(capDuration.isStart()){
                return;
            }
            cRotate();
            buz.on();
            sts = true;
            if(!capDuration.isStart()){
                capDuration.setType(0);
                capDuration.startCount();
            }
        }
    }
    void close(){
        if(sts==true){
            if(capDuration.isStart()){
                return;
            }
            anRotate();
            buz.on();
            sts = false;
            if(!capCloseDuration.isStart()){
                capCloseDuration.setType(1);
                capCloseDuration.startCount();
            }
        }
    }
    bool getStatus(){
        return sts;
    }
};

/*class Smotor:public Servo{
private:
    bool status;
    int pos,pin,oangle,cangle;
public:
    Smotor(int p,int o,int c):pin(p),pos(0),status(false),oangle(o),cangle(c){

    }

    void write(int p){
      pos=p;
      Servo::attach(pin);
      Servo::write(pos);
    }

    void detach(){
        Servo::detach();
    }

    void copen(bool s){
        if(s==true && !isOpen()){
            status = true;
            write(oangle);
            Serial.println("open..");
        }else if(!s&&isOpen()){
            status = false;
            write(cangle);
            capClose.stopCount();
            Serial.println("close..");
            bin.updateStatus();
            delay(100);
            String str = "{\"binId\":\""+ String(bin.binId) +"\",\"binStatus\":\""+ String(bin.getbins()) +"\"}";
            ep.write(str);
        }
    }

    bool isOpen(){
        return status;
    }
};*/

#endif
