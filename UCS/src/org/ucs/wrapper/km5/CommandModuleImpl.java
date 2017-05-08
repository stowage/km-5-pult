package org.ucs.wrapper.km5;

import org.ucs.wrapper.*;
import org.ucs.wrapper.km5.container.*;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 25.06.2005
 * Time: 20:29:12
 * To change this template use File | Settings | File Templates.
 */
public class CommandModuleImpl implements CommandModule{

    private SessionModule module;
    private IOHelperInterface helper;
    private int mode;

    public CommandModuleImpl(ConnectionModule conn) throws Exception {
        this.helper = new IOHelper();
        this.module = conn.getSession();
        this.mode = conn.getMode();
   }

     //cmd:0
    public void cmdNetAddr(DataContainer container, String addr) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)0);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();
        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadNetAddrContainer netAddr = (ReadNetAddrContainer)container;
        netAddr.netAddr = helper.readBCDAddr();
        netAddr.command = (int)helper.readUnsignedByte();
        netAddr.version = helper.readBCDByte();
        for (int i = 0; i<=20; i++) helper.readByte();
        netAddr.checkSum = helper.readChecksum();
    }

    //cmd:4
    public void cmdReadMonitor(DataContainer container, String addr) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)4);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadMonitorContainer monitor = (ReadMonitorContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.display = helper.readDisplay(16);
        monitor.isCancel = (int)helper.readByte();
        monitor.curPos = (int)helper.readByte();
        for (int i = 0; i<=6; i++) helper.readByte();
        monitor.csValue1 = helper.readUnsignedByte();
        monitor.csValue2 = helper.readUnsignedByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:5
    public void cmdSendKey(DataContainer container, String addr, byte key) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)5);
        helper.writeByte(key);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0});
        helper.writeChecksum();
        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE,  mode);
        ReadMonitorContainer monitor = (ReadMonitorContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.display = helper.readDisplay(16);
        monitor.isCancel = (int)helper.readByte();
        monitor.curPos = (int)helper.readByte();
        for (int i = 0; i<=6; i++) helper.readByte();
        monitor.csValue1 = helper.readUnsignedByte();
        monitor.csValue2 = helper.readUnsignedByte();
        monitor.checkSum = helper.readChecksum();

    }

    //cmd:8
    public void cmdReadStatus(DataContainer container, String addr) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)8);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadStatusContainer monitor = (ReadStatusContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.regimeGvs = (int)helper.readByte();
        monitor.byteEmptyPipe = helper.readByte();
        monitor.byteOfHardErr = helper.readByte();
        monitor.regimeGvsMode = helper.readByte();
        monitor.byteSettingUp = helper.readByte();
        monitor.byteErrDevice = helper.readByte();
        monitor.byteFlagOfData = helper.readByte();
        monitor.byteDeviceProperties = helper.readByte();
        for (int i = 0; i<=16; i++) helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:9
    public void cmdVersion(DataContainer container, String addr) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)9);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadVersionContainer monitor = (ReadVersionContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.version = helper.readASCII(5);
        monitor.max = helper.readByte();
        monitor.type = helper.readASCII(4);
        for (int i = 0; i<=14; i++) helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:2
    public void cmdCheck(DataContainer container, String addr, int responseType)throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)2);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadCheckContainer monitor = (ReadCheckContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        for (int k = 0; k<=6; k++) helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:25
    public void cmdCheckMath(DataContainer container, String addr, int mathOper, float data1, float data2) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)25);
        helper.writeByte((byte)mathOper);
        helper.writeFloat(data1);
        helper.writeFloat(data2);
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE,  mode);
        ReadCheckMathContainer monitor = (ReadCheckMathContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.result = helper.readFloat();
        for (int i=0; i<=26; i++) helper.readByte();
    }

    //cmd:41
    public void cmdEnableCompute(DataContainer container, String addr) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)41);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();
        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
    }

    //cmd:44
    public void cmdImmidiateValue1(DataContainer container, String addr, int type) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)44);
        helper.writeByte((byte)type);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_100, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadImmidiateValue1Container monitor = (ReadImmidiateValue1Container)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        switch(type) {
            case 0:
                monitor.eeh = helper.readByte();
                monitor.currentDay = Extractor.getIntDef(helper.readBCDByte(),0);
                monitor.currentMonth = Extractor.getIntDef(helper.readBCDByte(),0);
                monitor.currentYear = Extractor.getIntDef(helper.readBCDByte(),0);
                monitor.currentType= (int)helper.readByte();
                monitor.currentHour= Extractor.getIntDef(helper.readBCDByte(),0);
                monitor.currentMinute= Extractor.getIntDef(helper.readBCDByte(),0);
                monitor.currentSecond= Extractor.getIntDef(helper.readBCDByte(),0);
                break;
            case 1:
                monitor.currentHeat = helper.readFloat();
                break;
            case 2:
                monitor.currentM1 = helper.readFloat();
                break;
            case 3:
                monitor.currentM2 = helper.readFloat();
                break;
            case 4:
                monitor.workTime = helper.readFloat();
                break;
            case 5:
                monitor.currentG1m3 = helper.readFloat();
                monitor.currentG1T = helper.readFloat();
                monitor.currentP1 = helper.readFloat();
                monitor.currentP2 = helper.readFloat();
                monitor.currentt1 = helper.readFloat();
                monitor.currentt2 = helper.readFloat();
                break;
            case 6:
                monitor.currentUseT = helper.readFloat();
                break;
            case 7:
                monitor.currentG2m3 = helper.readFloat();
                monitor.currentG2T = helper.readFloat();
                monitor.currentG3m3 = helper.readFloat();
                monitor.currentG3T = helper.readFloat();
                monitor.currentt3 = helper.readFloat();
                monitor.currentt4 = helper.readFloat();
                break;
            case 8:
                monitor.currentGiKM = helper.readFloat();
                monitor.currentGiPPS = helper.readFloat();
                monitor.currentSpd = helper.readFloat();
                break;

        }
        monitor.checkSum = helper.readChecksum();
    }

    //cmd: 93
    public void cmdImmidiateValue2(DataContainer container, String addr, int cmdType) throws Exception {//cmd93 or 123
        helper.clearOut();
        helper.writeBCDAddr(addr);
        if (cmdType==93)  helper.writeByte((byte)93);
        else helper.writeByte((byte)123);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, cmdType==93?CommandModule.WAITIME_800:CommandModule.WAITIME_100);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.LONG_RESPONSE,  mode);
        ReadImmidiateValue2Container monitor = (ReadImmidiateValue2Container)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.zero = helper.readByte();
        monitor.g1 = helper.readFloat();
        monitor.g2 = helper.readFloat();
        monitor.g3 = helper.readFloat();
        monitor.t1 = helper.readFloat();
        monitor.t2 = helper.readFloat();
        monitor.tx = helper.readFloat();
        monitor.p1 = helper.readFloat();
        monitor.p2 = helper.readFloat();
        monitor.p3 = helper.readFloat();
        monitor.w = helper.readFloat();
        monitor.t2pps = helper.readFloat();
        monitor.txpps = helper.readFloat();
        monitor.tInDevice = helper.readFloat();
        monitor.w2 = helper.readFloat();
        monitor.tGvs = helper.readFloat();
        monitor.insideCounter = helper.readByte();
    }

    //cmd:50
    public void cmdReadTitleDB(DataContainer container, String addr, int dbType) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)50);
        helper.writeByte((byte)dbType);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);

        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadTitleDBContainer monitor = (ReadTitleDBContainer)container;
        monitor.netAddr= helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.addrStartDB = helper.readShort();
        monitor.numbEEPROM = (int)helper.readByte();
        monitor.specialFlags = helper.readByte();
        monitor.numberLineDB = helper.readShort();
        monitor.numberLastLine = helper.readShort();
        for (int i = 0; i<= 16;i++) helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:51
    public void cmdReadNumberLineDB(DataContainer container, String addr, int dbType) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)51);
        helper.writeByte((byte)dbType);
        helper.writeBytes(new byte[] {0,0,0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);

        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadNumberLineDBContainer monitor = (ReadNumberLineDBContainer)container;
        monitor.netAddr= helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.specialIndication = helper.readByte();
        monitor.numberEarlyLine = helper.readShort();

        monitor.eehEarly = helper.readByte();
        monitor.numbMonthEarly = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.monthEarly = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.yearEarly= Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.typeEarly= (int)helper.readByte();
        monitor.hoursEarly= Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutesEarly= Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.secondsEarly= Extractor.getIntDef(helper.readBCDByte(),0);

        monitor.numberLastLine = helper.readShort();

        monitor.eehLast = helper.readByte();
        monitor.numbMonthLast = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.monthLast = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.yearLast = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.typeLast = (int)helper.readByte();
        monitor.hoursLast = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutesLast = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.secondsLast = Extractor.getIntDef(helper.readBCDByte(),0);

        monitor.numberLineDB = helper.readShort();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:52
    public void cmdReadLineDataTimeDB(DataContainer container, String addr, int dbType, int date, int month, int year, int hour) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)52);
        helper.writeByte((byte)dbType);
        helper.writeBCDByte(""+date);
        helper.writeBCDByte(""+month);
        helper.writeBCDByte(""+year);
        helper.writeBCDByte(""+hour);
        helper.writeBytes(new byte[] {0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);

        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadLineDataTimeDBContainer monitor = (ReadLineDataTimeDBContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.specialIndicstion = helper.readByte();
        monitor.lineAddr = helper.readShort();

        monitor.eeh = helper.readByte();
        monitor.day = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.month = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.year = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.type = (int)helper.readByte();
        monitor.hours = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutes = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.seconds = Extractor.getIntDef(helper.readBCDByte(),0);
        for(int i=0; i<=13; i++)  helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:54
    public void cmdReadLineAbsAddrDB(DataContainer container, String addr, int addrStartDB, int numbEEPROM) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)54);
        helper.writeShort(addrStartDB);
        helper.writeByte((byte)numbEEPROM);
        helper.writeBytes(new byte[] {0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE,  mode);
        ReadLineAbsAddrDBContainer monitor = (ReadLineAbsAddrDBContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.codeEventOrErr = helper.readByte();

        monitor.eeh = helper.readByte();
        monitor.day = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.month = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.year = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.type = (int)helper.readByte();
        monitor.hours = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutes = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.seconds = Extractor.getIntDef(helper.readBCDByte(),0);

        for(int i=0; i<=15; i++) helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:55
    public void cmdReadLineNumberDBErr(DataContainer container, String addr, int numberLine) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)55);
        helper.writeByte((byte)4);
        helper.writeShort(numberLine);

        helper.writeBytes(new byte[] {0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.SHORT_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.SHORT_RESPONSE, mode);
        ReadLineNumberDBErrContainer monitor = (ReadLineNumberDBErrContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.codeEventOrErr = ((int)helper.readByte()) & 0xFF;

        monitor.day = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.month = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.year = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.type = (int)helper.readByte();
        monitor.hours = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutes = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.seconds = Extractor.getIntDef(helper.readBCDByte(),0);

        for(int i=0; i<=15; i++) helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:64
    public void cmdReadLineAddrEEPROMDB(DataContainer container, String addr, int addrEEPROM, int numbEEPROM) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)64);
        helper.writeShort(addrEEPROM);
        helper.writeByte((byte)numbEEPROM);

        helper.writeBytes(new byte[] {0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.LONG_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.LONG_RESPONSE, mode);
        ReadLineDBContainer monitor = (ReadLineDBContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.eeh = helper.readByte();
        monitor.day = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.month = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.year = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.type = (int)helper.readByte();
        monitor.hours = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutes = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.seconds = Extractor.getIntDef(helper.readBCDByte(),0);

        monitor.ta = helper.readFloat();
        monitor.P1 = helper.readFloat();
        monitor.P2 = helper.readFloat();
        monitor.P3t3 = helper.readFloat();
        monitor.t1 = helper.readFloat();
        monitor.t2 = helper.readFloat();
        monitor.t3t4 = helper.readFloat();
        monitor.M1 = helper.readFloat();
        monitor.M2 = helper.readFloat();
        monitor.ViQgvs = helper.readFloat();
        monitor.V1M3 = helper.readFloat();
        monitor.V2M4 = helper.readFloat();
        monitor.Q = helper.readFloat();
        monitor.Tp = helper.readFloat();
        helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }

    //cmd:65
    public void cmdReadLineDB(DataContainer container, String addr, int dbType, int addrLine) throws Exception {
        helper.clearOut();
        helper.writeBCDAddr(addr);
        helper.writeByte((byte)65);
        helper.writeByte((byte)dbType);
        helper.writeShort(addrLine);

        helper.writeBytes(new byte[] {0,0,0,0,0,0});
        helper.writeChecksum();

        module.setConstraint(CommandModule.WAITIME_300, CommandModule.LONG_RESPONSE);
        helper.writeToOut(module.getOutput(),CommandModule.COMMAND_LENGTH, mode);
        helper.readIn(module.getInput(),CommandModule.LONG_RESPONSE, mode);

        ReadLineDBContainer monitor = (ReadLineDBContainer)container;
        monitor.netAddr = helper.readBCDAddr();
        monitor.command = (int)helper.readUnsignedByte();

        monitor.eeh = helper.readByte();
        monitor.day = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.month = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.year = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.type = (int)helper.readByte();
        monitor.hours = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.minutes = Extractor.getIntDef(helper.readBCDByte(),0);
        monitor.seconds = Extractor.getIntDef(helper.readBCDByte(),0);

        monitor.ta = helper.readFloat();
        monitor.P1 = helper.readFloat();
        monitor.P2 = helper.readFloat();
        monitor.P3t3 = helper.readFloat();
        monitor.t1 = helper.readFloat();
        monitor.t2 = helper.readFloat();
        monitor.t3t4 = helper.readFloat();
        monitor.M1 = helper.readFloat();
        monitor.M2 = helper.readFloat();
        monitor.ViQgvs = helper.readFloat();
        monitor.V1M3 = helper.readFloat();
        monitor.V2M4 = helper.readFloat();
        monitor.Q = helper.readFloat();
        monitor.Tp = helper.readFloat();
        helper.readByte();
        monitor.checkSum = helper.readChecksum();
    }
}
