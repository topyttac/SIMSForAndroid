package simms.biosci.simsapplication.Manager;

import android.content.Context;
import android.content.Intent;

/**
 * Created by User 2 on 6/22/2017.
 */

public class ScannerInterface {
    //´ò¿ªÓë¹Ø±ÕÉ¨ÃèÍ·
    public static final String KEY_BARCODE_ENABLESCANNER_ACTION = "android.intent.action.BARCODESCAN";
    //¿ªÊ¼É¨Ãè
    public static final String KEY_BARCODE_STARTSCAN_ACTION = "android.intent.action.BARCODESTARTSCAN";
    //Í£Ö¹É¨Ãè
    public static final String KEY_BARCODE_STOPSCAN_ACTION = "android.intent.action.BARCODESTOPSCAN";

    //Ëø¶¨É¨Ãè¼ü
    public static final String KEY_LOCK_SCAN_ACTION = "android.intent.action.BARCODELOCKSCANKEY";
    //½âËøÉ¨Ãè¼ü
    public static final String KEY_UNLOCK_SCAN_ACTION = "android.intent.action.BARCODEUNLOCKSCANKEY";
    //ÉùÒôandroid.intent.action.BEEP
    public static final String KEY_BEEP_ACTION = "android.intent.action.BEEP";
    //É¨ÃèÊ§°ÜÌáÊ¾Òô
    public static final String KEY_FAILUREBEEP_ACTION = "android.intent.action.FAILUREBEEP";
    //Õð¶¯android.intent.action.VIBRATE
    public static final String KEY_VIBRATE_ACTION = "android.intent.action.VIBRATE";
    //ÊÇ·ñ¹ã²¥Ä£Ê½
    public static final String KEY_OUTPUT_ACTION = "android.intent.action.BARCODEOUTPUT";
    //¹ã²¥ÉèÖÃ±àÂë¸ñÊ½
    public static final String KEY_CHARSET_ACTION = "android.intent.actionCHARSET";
    //Ê¡µçÄ£Ê½
    public static final String KEY_POWER_ACTION = "android.intent.action.POWER";
    //¸½¼ÓÄÚÈÝ
    public static final String KEY_TERMINATOR_ACTION = "android.intent.TERMINATOR";
    //Í¨ÖªÀ¸Í¼±êÏÔÊ¾android.intent.action.SHOWNOTICEICON
    public static final String KEY_SHOWNOTICEICON_ACTION = "android.intent.action.SHOWNOTICEICON";
    //APPÍ¼±êÏÔÊ¾android.intent.action.SHOWAPPICON
    public static final String KEY_SHOWICON_ACTION = "android.intent.action.SHOWAPPICON";

    //´ò¿ªÉ¨ÃèÉèÖÃ½çÃæ
    public static final String KEY_SHOWISCANUI = "com.android.auto.iscan.show_setting_ui";

    //Ìí¼ÓÇ°×º
    public static final String KEY_PREFIX_ACTION = "android.intent.action.PREFIX";
    //ºó×º
    public static final String KEY_SUFFIX_ACTION = "android.intent.action.SUFFIX";
    //½ØÈ¡×ó×Ö·û
    public static final String KEY_TRIMLEFT_ACTION = "android.intent.action.TRIMLEFT";
    //½ØÈ¡ÓÒ×Ö·û
    public static final String KEY_TRIMRIGHT_ACTION = "android.intent.action.TRIMRIGHT";
    //ÓÒÉÏ²àLedµÆ¹â¿ØÖÆ
    public static final String KEY_LIGHT_ACTION = "android.intent.action.LIGHT";
    //ÉèÖÃ³¬Ê±Ê±¼ä
    public static final String KEY_TIMEOUT_ACTION = "android.intent.action.TIMEOUT";
    //¹ýÂËÌØ¶¨×Ö·û
    public static final String KEY_FILTERCHARACTER_ACTION = "android.intent.action.FILTERCHARACTER";
    //Á¬É¨
    public static final String KEY_CONTINUCESCAN_ACTION = "android.intent.action.BARCODECONTINUCESCAN";
    //Á¬ÐøÉ¨Ãè¼ä¸ôÊ±¼ä
    public static final String KEY_INTERVALTIME_ACTION = "android.intent.action.INTERVALTIME";
    //ÊÇ·ñÉ¾³ý±à¼­¿òÄÚÈÝ
    public static final String KEY_DELELCTED_ACTION = "android.intent.action.DELELCTED";
    //»Ö¸´Ä¬ÈÏÉèÖÃ
    public static final String KEY_RESET_ACTION = "android.intent.action.RESET";
    //É¨Ãè°´¼üÅäÖÃ
    public static final String SCANKEY_CONFIG_ACTION = "android.intent.action.scankeyConfig";

    //É¨ÃèÊ§°Ü¹ã²¥
    public static final String KEY_FAILUREBROADCAST_ACTION = "android.intent.action.FAILUREBROADCAST";

    /****************************************************************************************************/

    /********************************************ÏµÍ³½Ó¿Ú¶¨Òå³£Á¿******************************/
    static final String SET_STATUSBAR_EXPAND = "com.android.set.statusbar_expand";
    static final String SET_USB_DEBUG = "com.android.set.usb_debug";
    static final String SET_INSTALL_PACKAGE = "com.android.set.install.package";
    static final String SET_SCREEN_LOCK = "com.android.set.screen_lock";
    static final String SET_CFG_WAKEUP_ANYKEY = "com.android.set.cfg.wakeup.anykey";
    static final String SET_UNINSTALL_PACKAGE = "com.android.set.uninstall.package";
    static final String SET_SYSTEM_TIME = "com.android.set.system.time";
    static final String SET_KEYBOARD_CHANGE = "com.android.disable.keyboard.change";
    static final String SET_INSTALL_PACKAGE_WITH_SILENCE = "com.android.set.install.packege.with.silence";
    static final String SET_INSTALL_PACKAGE_EXTRA_APK_PATH = "com.android.set.install.packege.extra.apk.path";
    static final String SET_INSTALL_PACKAGE_EXTRA_TIPS_FORMAT = "com.android.set.install.packege.extra.tips.format";
    static final String SET_SIMULATION_KEYBOARD = "com.android.simulation.keyboard";
    static final String SET_SIMULATION_KEYBOARD_STRING = "com.android.simulation.keyboard.string";
    /****************************************************************************************************/

    private Context mContext;
    private static ScannerInterface androidjni;

    public ScannerInterface(Context context) {
        mContext = context;

    }

    /*********É¨Ãè ¿ØÖÆ½Ó¿Ú*********************/

    /*************************************/

    //	1.´ò¿ªÉ¨ÃèÉèÖÃ½çÃæ
    public void ShowUI() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_SHOWISCANUI);
            mContext.sendBroadcast(intent);
        }
    }

    //	2.´ò¿ªÉ¨ÃèÍ·µçÔ´
    public void open() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_BARCODE_ENABLESCANNER_ACTION);
            intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, true);
            mContext.sendBroadcast(intent);
        }
    }


    public void close() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_BARCODE_ENABLESCANNER_ACTION);
            intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, false);
            mContext.sendBroadcast(intent);
        }

    }


    public void scan_start() {

        if (mContext != null) {
            Intent intent = new Intent(KEY_BARCODE_STARTSCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    //4.Í£Ö¹É¨ÃèÍ·½âÂë£¬É¨ÃèÍ·Ãð¹â

    /**
     * ´Ëº¯ÊýºÍ scan_stop ÅäºÏÊ¹ÓÃ¿ÉÒÔÔÚ³ÌÐòÖÐÈí¼þ´¥·¢É¨ÃèÍ·¡£µ±Ó¦ÓÃ³ÌÐòµ÷ÓÃ
     * scan_start ´¥·¢É¨ÃèÍ·³ö¹âÉ¨Ãèºó, ±ØÐëµ÷ÓÃ scan_stop »Ö¸´É¨ÃèÍ·×´Ì¬¡£
     */
    public void scan_stop() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_BARCODE_STOPSCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * Ëø¶¨Éè±¸µÄÉ¨Ãè°´¼ü£¬Ëø¶¨ºó£¬Ö»ÄÜÍ¨¹ýiScan¶¨ÒåµÄÉ¨Ãè°´¼ü¿ØÖÆÉ¨Ãè£¬ÓÃ»§ÎÞ·¨×Ô¶¨Òå°´¼ü¡£
     */
    public void lockScanKey() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_LOCK_SCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    /******
     * ½â³ý¶ÔÉ¨Ãè°´¼üµÄËø¶¨¡£½â³ýºóiScanÎÞ·¨¿ØÖÆÉ¨Ãè¼ü£¬ÓÃ»§¿É×Ô¶¨Òå°´¼ü¡£
     */
    public void unlockScanKey() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_UNLOCK_SCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * KEY_CHARSET_ACTION   ¹ã²¥ÉèÖÃ±àÂë¸ñÊ½
     * 0  <item>ASCII</item>
     * 1  <item>GB2312</item>
     * 2  <item>GBK</item>
     * 3  <item>GB18030</item>
     * 4  <item>UTF-8</item>
     */

    public void setCharSetMode(int mode) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_CHARSET_ACTION);
            intent.putExtra(KEY_CHARSET_ACTION, mode);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * É¨ÃèÍ·µÄÊä³öÄ£Ê½
     * mode 0:É¨Ãè½á¹ûÖ±½Ó·¢ËÍµ½½¹µã±à¼­¿òÄÚ
     * mode 1:É¨Ãè½á¹ûÒÔ¹ã²¥Ä£Ê½·¢ËÍ£¬Ó¦ÓÃ³ÌÐòÐèÒª×¢²áactionÎª¡°android.intent.action.SCANRESULT¡±µÄ¹ã²¥½ÓÊÕÆ÷£¬ÔÚ¹ã²¥»úµÄ onReceive(Context context, Intent arg1) ·½·¨ÖÐ,Í¨¹ýÈçÏÂÓï¾ä
     * String  barocode=arg1.getStringExtra("value");
     * int barocodelen=arg1.getIntExtra("length",0);
     * ·Ö±ð»ñµÃ ÌõÂëÖµ,ÌõÂë³¤¶È,ÌõÂëÀàÐÍ
     */
    public void setOutputMode(int mode) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_OUTPUT_ACTION);
            intent.putExtra(KEY_OUTPUT_ACTION, mode);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 8 ÊÇ·ñ²¥·ÅÉùÒô
     */
    public void enablePlayBeep(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_BEEP_ACTION);
            intent.putExtra(KEY_BEEP_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * É¨ÃèÊ§°ÜÊÇ·ñ²¥·ÅÉùÒô
     */
    public void enableFailurePlayBeep(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_FAILUREBEEP_ACTION);
            intent.putExtra(KEY_FAILUREBEEP_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }


    /**
     * 9 ÊÇ·ñÕð¶¯
     */
    public void enablePlayVibrate(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_VIBRATE_ACTION);
            intent.putExtra(KEY_VIBRATE_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * KEY_POWER_ACTION   Ê¡µçÄ£Ê½
     * true ¿ªÆô£¬¸ÃÄ£Ê½ÏÂÆÁÄ»ËøÆÁºó½«ÎÞ·¨É¨Ãè
     * false ¹Ø±Õ£¬¸ÃÄ£Ê½Ïà·´
     */
    public void enablePower(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_POWER_ACTION);
            intent.putExtra(KEY_POWER_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * ¸½¼Ó»Ø³µ¡¢»»ÐÐµÈ
     * 0 <item>ÎÞ</item>
     * 1 <item>¸½¼Ó»Ø³µ¼ü</item>
     * 2 <item>¸½¼ÓTAB¼ü</item>
     * 3 <item>¸½¼Ó»»ÐÐ·û</item>
     */
    public void enableAddKeyValue(int value) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_TERMINATOR_ACTION);
            intent.putExtra(KEY_TERMINATOR_ACTION, value);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 11  Í¨ÖªÀ¸Í¼±ê KEY_SHOWNOTICEICON_ACTION
     * //enable £ºtrue  Òþ²Ø   false:
     */

    public void enablShowNoticeIcon(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_SHOWNOTICEICON_ACTION);
            intent.putExtra(KEY_SHOWNOTICEICON_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 12 app ×ÀÃæÍ¼±ê KEY_SHOWICON_ACTION
     * // enable £ºtrue  Òþ²Ø   false:
     */
    public void enablShowAPPIcon(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_SHOWICON_ACTION);
            intent.putExtra(KEY_SHOWICON_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /************************************************************/

    //KEY_PREFIX_ACTION Ìí¼ÓÇ°×º
    public void addPrefix(String text) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_PREFIX_ACTION);
            intent.putExtra(KEY_PREFIX_ACTION, text);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_SUFFIX_ACTIONÌí¼Óºó×º
    public void addSuffix(String text) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_SUFFIX_ACTION);
            intent.putExtra(KEY_SUFFIX_ACTION, text);
            mContext.sendBroadcast(intent);
        }
    }

    //½ØÈ¡×ó×Ö·ûKEY_TRIMLEFT_ACTION
    public void interceptTrimleft(int num) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_TRIMLEFT_ACTION);
            intent.putExtra(KEY_TRIMLEFT_ACTION, num);
            mContext.sendBroadcast(intent);
        }
    }

    //½ØÈ¡ÓÒ×Ö·ûKEY_TRIMRIGHT_ACTION
    public void interceptTrimright(int num) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_TRIMRIGHT_ACTION);
            intent.putExtra(KEY_TRIMRIGHT_ACTION, num);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_LIGHT_ACTION ÓÒ²àLedµÆ¹â¿ØÖÆ
    public void lightSet(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_LIGHT_ACTION);
            intent.putExtra(KEY_LIGHT_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_TIMEOUT_ACTION ÉèÖÃ³¬Ê±Ê±¼ä
    public void timeOutSet(int value) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_TIMEOUT_ACTION);
            intent.putExtra(KEY_TIMEOUT_ACTION, value);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_FILTERCHARACTER_ACTION  //¹ýÂËÌØ¶¨×Ö·û
    public void filterCharacter(String text) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_FILTERCHARACTER_ACTION);
            intent.putExtra(KEY_FILTERCHARACTER_ACTION, text);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_CONTINUCESCAN_ACTION  ÊÇ·ñÁ¬É¨
    public void continceScan(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_CONTINUCESCAN_ACTION);
            intent.putExtra(KEY_CONTINUCESCAN_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_INTERVALTIME_ACTION  Á¬ÐøÉ¨Ãè¼ä¸ôÊ±¼ä
    public void intervalSet(int value) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_INTERVALTIME_ACTION);
            intent.putExtra(KEY_INTERVALTIME_ACTION, value);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_FAILUREBROADCAST_ACTION É¨ÃèÊ§°Ü¹ã²¥
    public void SetErrorBroadCast(boolean enable) {
        if (mContext != null) {
            Intent intent = new Intent(KEY_FAILUREBROADCAST_ACTION);
            intent.putExtra(KEY_FAILUREBROADCAST_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_RESET_ACTION  »Ö¸´Ä¬ÈÏÉèÖÃ
    public void resultScan() {
        if (mContext != null) {
            Intent intent = new Intent(KEY_RESET_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    //SCANKEY_CONFIG_ACTION   É¨Ãè°´¼üÅäÖÃ
    //KEYCODE °´¼üÃû³Æ   valueÓÐÁ½¸ö0£¬1  0±íÊ¾É¨Ãè£¬1²»×öÈÎºÎ²Ù×÷
    public void scanKeySet(int keycode, int value) {
        if (mContext != null) {
            Intent intent = new Intent(SCANKEY_CONFIG_ACTION);
            intent.putExtra("KEYCODE", keycode);
            intent.putExtra("value", value);
            mContext.sendBroadcast(intent);
        }
    }
}
