#include <SoftwareSerial.h>
#ifndef _default_h
#define _default_h

#define null "\0"

const int rxPin = 2;
const int txPin = 3;

SoftwareSerial espSerial(rxPin,txPin);

class buzzer{
    int pin;
    bool sts;
public:
    buzzer(int p):pin(p){
        pinMode(p, OUTPUT);
        off();
    }
    void on(){
        digitalWrite(pin,HIGH);
        sts = true;
    }
    void off(){
        digitalWrite(pin,LOW);
        sts = false;
    }
    bool isOn(){
        return sts;
    }
};

class nrDelay{
private:
    int ptime,dtime,ctime;
    int type;
    bool flagCount;
    void update(){
        ctime = millis();
    }
    void setPreviousTime(){
        ptime = millis();
    }
    void reset(){
        ptime = 0;
        ctime = 0;
    }
public:
    nrDelay(int diffTime):ptime(0),dtime(diffTime),ctime(0),type(0),flagCount(false){

    }
    void startCount(){
        flagCount = true;
        setPreviousTime();
    }
    void stopCount(){
        flagCount = false;
        reset();
    }

    bool isDelayComplete(){
        update();
        if((ctime-ptime) >= dtime)
            return true;
        return false;
    }
    bool isStart(){
        return flagCount;
    }
    void setType(int t){
        type = t;
    }
    int getType(){
        return type;
    }
};

class esp{
private:
public:
    esp(){
    }
    void begin(){
        espSerial.begin(9600);
    }
    String read(){
        String line = null;
        if(espSerial.available()){
            while(espSerial.available()){
                while(!espSerial.available());
                char c = espSerial.read();
                if(c=='\n')
                    break;
                line += String(c);
            }
        }
        return line;
    }
    void write(String str){
        str = str+"#";
        if(str!=null)
            espSerial.print(str);
            espSerial.flush();
    }
};

#endif
