package org.ucs.wrapper;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 17.06.2005
 * Time: 21:25:25
 * To change this template use File | Settings | File Templates.
 */
public interface CommandModule {
    public static final int  COMMAND_LENGTH = 16;

    public static final int TINY_RESPONSE = 8;
    public static final int SHORT_RESPONSE = 32;
    public static final int LONG_RESPONSE = 72;

    public static final int WAITIME_100 = 100;
    public static final int WAITIME_300 = 300;
    public static final int WAITIME_800 = 800;

    public static final int N_DATA_TIME = 0;
    public static final int N_WARMTH = 1;
    public static final int N_MASS1 = 2;
    public static final int N_MASS2 = 3;
    public static final int N_WORKING_TIME = 4;
    public static final int N_CONSUPTION1 = 5;
    public static final int N_OUTPUT = 6;
    public static final int N_CONSUPTION2 = 7;
    public static final int N_REL_CONSUPTION = 8;

    public void cmdNetAddr(DataContainer container, String addr) throws Exception;          //cmd0
    public void cmdReadMonitor(DataContainer container, String addr) throws Exception;      //cmd4
    public void cmdSendKey(DataContainer container, String addr, byte key) throws Exception;          //cmd5
    public void cmdReadStatus(DataContainer container, String addr)throws Exception;       //cmd8
    public void cmdVersion(DataContainer container, String addr) throws Exception;          //cmd9-44
    public void cmdCheck(DataContainer container, String addr, int responseType)throws Exception; //cmd2
    public void cmdCheckMath(DataContainer container, String addr, int mathOper, float data1, float data2) throws Exception;  //cmd25
    public void cmdEnableCompute(DataContainer container, String addr) throws Exception;//cmd 41
    public void cmdImmidiateValue1(DataContainer container, String addr, int type) throws Exception;//cmd44
    public void cmdImmidiateValue2(DataContainer container, String addr, int cmdType) throws Exception;//cmd93 or 123
    public void cmdReadTitleDB(DataContainer container, String addr, int dbType) throws Exception;//cmd50
    public void cmdReadNumberLineDB(DataContainer container, String addr, int dbType) throws Exception;//51
    public void cmdReadLineDataTimeDB(DataContainer container, String addr, int dbType, int date, int month, int year, int hour) throws Exception;//52
    public void cmdReadLineAbsAddrDB(DataContainer container, String addr, int addrStartDB, int numbEEPROM) throws Exception;//54
    public void cmdReadLineNumberDBErr(DataContainer container, String addr, int numberLine) throws Exception;//55
    public void cmdReadLineAddrEEPROMDB(DataContainer container, String addr, int addrEEPROM, int numbEEPROM) throws Exception;//64
    public void cmdReadLineDB(DataContainer container, String addr, int dbType, int addrLine) throws Exception;//65
}
