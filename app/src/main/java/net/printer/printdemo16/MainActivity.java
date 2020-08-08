package net.printer.printdemo16;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.DataForSendToPrinterTSC;
import net.posprinter.utils.PosPrinterDev;
import net.printer.printdemo16.ReceiptPrinter.R58Activity;
import net.printer.printdemo16.ReceiptPrinter.R80Activity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.printer.printdemo16.pojo.XmlPojo;
import net.printer.printdemo16.utils.Base64Util;
import net.printer.printdemo16.utils.CommonUtil;
import net.printer.printdemo16.utils.ConstantValue;
import net.printer.printdemo16.utils.DatabaseHelper;
import net.printer.printdemo16.utils.ExcelUtil;
import net.printer.printdemo16.utils.FileUtil;
import net.printer.printdemo16.utils.HttpUtil;
import net.printer.printdemo16.utils.MyApp;
import net.printer.printdemo16.utils.TokenInfo;
import net.printer.printdemo16.utils.WordInfo;
import net.printer.printdemo16.utils.XmlParseUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Log logger;
    public static IMyBinder myBinder;
    public static final String TABLE_NAME = "void_ocr";

    ServiceConnection mSerconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (IMyBinder) service;
            Log.e("myBinder", "connect");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("myBinder", "disconnect");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.i("MainActivity.onCreate", "********************-- MainActivity.onCreate[start] --********************");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind service，get imyBinder
        Intent intent = new Intent(this, PosprinterService.class);
        bindService(intent, mSerconnection, BIND_AUTO_CREATE);

        initView();
        logger.i("MainActivity.onCreate", "********************-- MainActivity.onCreate[end] --********************");
    }

    private Spinner port;
    private TextView adrress,tv_output;
    private EditText ip_adrress, tv_car_no, editAreaNo, editTextTime;
    private Button connect, disconnect, pos58, pos80, tsc80, other, btn_open_ocr_ui, btn_print;
    private int portType = 0;//0是网络，1是蓝牙，2是USB
    public static boolean ISCONNECT = false;

    private void initView() {
        port = findViewById(R.id.sp_port);
        adrress = findViewById(R.id.tv_address);
        tv_car_no = findViewById(R.id.tv_car_no);
        editAreaNo = findViewById(R.id.editAreaNo);
        editTextTime = findViewById(R.id.editTextTime);

        ip_adrress = findViewById(R.id.et_address);
        connect = findViewById(R.id.connect);
        disconnect = findViewById(R.id.disconnect);
//        tsc80 = findViewById(R.id.bt_tsc80);
        btn_open_ocr_ui = findViewById(R.id.btn_open_ocr_ui);
        btn_print = findViewById(R.id.btn_print);
        tv_output = findViewById(R.id.tv_output);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        editTextTime.setText(sdf.format(new Date()));
        editAreaNo.setTransformationMethod(new ReplacementTransformationMethod() {
            @Override
            protected char[] getOriginal() {
                char[] originalCharArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
                return originalCharArr;
            }

            @Override
            protected char[] getReplacement() {
                char[] replacementCharArr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return replacementCharArr;
            }
        });

        port.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                portType = i;
                switch (i) {
                    case 0:
                        ip_adrress.setVisibility(View.VISIBLE);
                        adrress.setVisibility(View.GONE);
                        break;
                    case 1:
                        ip_adrress.setVisibility(View.GONE);
                        adrress.setVisibility(View.VISIBLE);
                        adrress.setText("");
                        break;
                    case 2:
                        ip_adrress.setVisibility(View.GONE);
                        adrress.setVisibility(View.VISIBLE);
                        adrress.setText("");
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        connect.setOnClickListener(this);
        disconnect.setOnClickListener(this);
//        tsc80.setOnClickListener(this);
        adrress.setOnClickListener(this);
        btn_open_ocr_ui.setOnClickListener(this);
        btn_print.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.connect) {
            switch (portType) {
                case 0:
                    connectNet();
                    break;
                case 1:
                    connectBT();
                    break;
                case 2:
                    connectUSB();
                    break;
            }
        }

        if (id == R.id.disconnect) {
            disConnect();
        }

        if (id == R.id.tv_address) {

            switch (portType) {
                case 1:
                    setBluetooth();
                    break;
                case 2:
                    setUSB();
                    break;
            }
        }

        if (id == R.id.btn_open_ocr_ui) {
            openCameraByBaidu();
        }
        if (id == R.id.btn_print) {
            if (ISCONNECT) {
                SQLiteDatabase db = null;
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this, "void_ocr", null, 1);
                    db = dbHelper.getWritableDatabase();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String carNo = null;
                String areaNo = null;
                String timeStr = null;

                XmlPojo xmlPojo = new XmlPojo();
                List<XmlPojo> xmlPojos = new ArrayList<>();

                try {
                    carNo = tv_car_no.getText().toString();
                    areaNo = (editAreaNo.getText().toString()).toUpperCase();
                    timeStr = editTextTime.getText().toString();

                    xmlPojo.setAreaNo(areaNo);
                    xmlPojo.setCarNo(carNo);
                    xmlPojo.setTimeStr(timeStr);

                    xmlPojos.add(xmlPojo);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (carNo != null && carNo.length() > 0) {
                    if (areaNo != null && areaNo.length() > 0) {
                        StringBuilder builder = new StringBuilder();
                        String printText = builder.append(areaNo + "-").append(carNo + "-").append(timeStr).toString();

                        carNo = "车架号:" + carNo;
                        areaNo = "场位:" + areaNo;
                        timeStr = "入库时间:" + timeStr;
                        printText(carNo, areaNo, timeStr, printText);

                        tv_car_no.setText("");
                        editAreaNo.setText("");

                        addToDb(db, xmlPojo);
                        getFromDb(db, xmlPojo.getTimeStr());


                    } else {
                        Toast.makeText(getApplicationContext(), "请输入场位号", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请扫描车架号", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.connect_first), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addToDb(SQLiteDatabase db, XmlPojo xmlPojo) {
        logger.i("MainActivity.addToDb", "********************-- MainActivity.addToDb[start] --********************");
        try {
            ContentValues values = new ContentValues();
            values.put("car_area", xmlPojo.getAreaNo());
            values.put("car_no", xmlPojo.getCarNo());
            values.put("in_time", xmlPojo.getTimeStr());
            db.insert("void_ocr", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.i("MainActivity.addToDb", "********************-- MainActivity.addToDb[end] --********************");
    }

    public List<XmlPojo> getFromDb(SQLiteDatabase db, String in_time) {
        logger.i("MainActivity.getFromDb", "********************-- MainActivity.getFromDb[start] --********************");
        /**
         * db.query(tableName,null1,null2,null3,null4,null5,null6);
         * select null1 from tableName where null2=null3 group by null4 having null5 order by null6
         */
        Cursor cursor = null;
        String textview_data = null;
        List<XmlPojo> xmlPojos = new ArrayList<>();
        try {
            cursor = db.query(TABLE_NAME, null, "in_time =?", new String[]{in_time}, null, null, null);
            textview_data = "";
            while (cursor.moveToNext()) {
                String[] columnNames = cursor.getColumnNames();
                XmlPojo xmlPojo = new XmlPojo();
                for (String key : columnNames) {
                    logger.i("columnNames",key);
                    String value = cursor.getString(cursor.getColumnIndex(key));
                    switch (key) {
                        case "car_area":
                            xmlPojo.setAreaNo(value);
                            break;
                        case "car_no":
                            xmlPojo.setCarNo(value);
                            break;
                        case "in_time":
                            xmlPojo.setTimeStr(value);
                            break;
                    }
                    logger.i(key,value);
                }
                xmlPojos.add(xmlPojo);
                doExcel(xmlPojos);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭游标，释放资源
            logger.i("MainActivity.getFromDb", "********************-- MainActivity.getFromDb[end] --********************");
            cursor.close();
        }
        return null;
    }

    public void doExcel(List<XmlPojo> xmlPojos){
        logger.i("MainActivity.doExcel","********************-- MainActivity.doExcel[start] --********************");
        try {
            String filePath = Environment.DIRECTORY_DOWNLOADS;
//            String filePath = Environment.getExternalStorageState();
//            String filePath = this.getFilesDir().getPath().toString();
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String[] title = {"车架号", "场位", "入库时间"};
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String timeStr = sdf.format(new Date());
            String fileName = timeStr + ".xls";
            filePath = filePath +"/"+ fileName;
            ExcelUtil.initExcel(fileName,title);
            ExcelUtil.writeObjListToExcel(xmlPojos, filePath, this);
            logger.i("Excel文件已导出",filePath);
            tv_output.setText("Excel文件已导出: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.i("MainActivity.doExcel", "********************-- MainActivity.doExcel[end] --********************");
    }

    private void printText(final String carNo, final String areaNo, final String timeNo, final String printText) {
        logger.i("MainActivity.printText", "********************-- MainActivity.printText[start] --********************");
        logger.i("carNo", carNo);
        logger.i("areaNo", areaNo);
        logger.i("timeNo", timeNo);

        if (MainActivity.ISCONNECT) {

            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(), getString(R.string.send_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(), getString(R.string.send_failed), Toast.LENGTH_SHORT).show();

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    String carNoStr = carNo;
                    String areaNoStr = areaNo;
                    String timeNoStr = timeNo;
                    List<byte[]> list = new ArrayList<>();
                    //设置标签纸大小
                    list.add(DataForSendToPrinterTSC.sizeBymm(80, 50));
                    //设置间隙
                    list.add(DataForSendToPrinterTSC.gapBymm(0, 0));
                    //清除缓存
                    list.add(DataForSendToPrinterTSC.cls());
                    //设置方向
                    list.add(DataForSendToPrinterTSC.direction(0));
                    //线条
//                    list.add(DataForSendToPrinterTSC.bar(10,10,200,3));
                    //条码
                    //文本
                    list.add(DataForSendToPrinterTSC.text(10, 60, "TSS24.BF2", 0, 1, 1, carNo));
                    list.add(DataForSendToPrinterTSC.text(10, 120, "TSS24.BF2", 0, 1, 1, areaNo));
                    list.add(DataForSendToPrinterTSC.text(10, 180, "TSS24.BF2", 0, 1, 1, timeNo));
                    list.add(DataForSendToPrinterTSC.qrCode(320, 100, "M", 4, "A", 0, "M1", "S3", printText));

                    //打印
                    list.add(DataForSendToPrinterTSC.print(1));

                    return list;
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.connect_first), Toast.LENGTH_SHORT).show();
        }
        logger.i("MainActivity.printText", "********************-- MainActivity.printText[end] --********************");
    }

    /**
     * 网络连接
     */
    private void connectNet() {
        String ip = ip_adrress.getText().toString();
        if (ip != null || ISCONNECT == false) {
            myBinder.ConnectNetPort(ip, 9100, new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), getString(R.string.con_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 连接蓝牙
     */
    private void connectBT() {
        String BtAdress = adrress.getText().toString().trim();
        if (BtAdress.equals(null) || BtAdress.equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
        } else {
            myBinder.ConnectBtPort(BtAdress, new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), getString(R.string.con_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 连接usb
     */
    private void connectUSB() {
        String usbAddress = adrress.getText().toString().trim();
        if (usbAddress.equals(null) || usbAddress.equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.discon), Toast.LENGTH_SHORT).show();
        } else {
            myBinder.ConnectUsbPort(getApplicationContext(), usbAddress, new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), getString(R.string.connect), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.discon), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 断开连接
     */
    private void disConnect() {
        if (ISCONNECT) {
            myBinder.DisconnectCurrentPort(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), "disconnect ok", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), "disconnect failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private List<String> btList = new ArrayList<>();
    private ArrayList<String> btFoundList = new ArrayList<>();
    private ArrayAdapter<String> BtBoudAdapter, BtfoundAdapter;
    private View BtDialogView;
    private ListView BtBoundLv, BtFoundLv;
    private LinearLayout ll_BtFound;
    private AlertDialog btdialog;
    private Button btScan;
    private DeviceReceiver BtReciever;
    private BluetoothAdapter bluetoothAdapter;

    /*
    选择蓝牙设备
     */

    public void setBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断时候打开蓝牙设备
        if (!bluetoothAdapter.isEnabled()) {
            //请求用户开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        } else {

            showblueboothlist();

        }
    }

    private void showblueboothlist() {
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        BtDialogView = inflater.inflate(R.layout.printer_list, null);
        BtBoudAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btList);
        BtBoundLv = BtDialogView.findViewById(R.id.listView1);
        btScan = BtDialogView.findViewById(R.id.btn_scan);
        ll_BtFound = BtDialogView.findViewById(R.id.ll1);
        BtFoundLv = (ListView) BtDialogView.findViewById(R.id.listView2);
        BtfoundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btFoundList);
        BtBoundLv.setAdapter(BtBoudAdapter);
        BtFoundLv.setAdapter(BtfoundAdapter);
        btdialog = new AlertDialog.Builder(this).setTitle("BLE").setView(BtDialogView).create();
        btdialog.show();

        BtReciever = new DeviceReceiver(btFoundList, BtfoundAdapter, BtFoundLv);

        //注册蓝牙广播接收者
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(BtReciever, filterStart);
        registerReceiver(BtReciever, filterEnd);

        setDlistener();
        findAvalibleDevice();
    }

    private void setDlistener() {
        // TODO Auto-generated method stub
        btScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_BtFound.setVisibility(View.VISIBLE);
                //btn_scan.setVisibility(View.GONE);
            }
        });
        //已配对的设备的点击连接
        BtBoundLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();

                    }

                    String mac = btList.get(arg2);
                    mac = mac.substring(mac.length() - 17);
//                    String name=msg.substring(0, msg.length()-18);
                    //lv1.setSelection(arg2);
                    btdialog.cancel();
                    adrress.setText(mac);
                    //Log.i("TAG", "mac="+mac);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        //未配对的设备，点击，配对，再连接
        BtFoundLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();

                    }
                    String mac;
                    String msg = btFoundList.get(arg2);
                    mac = msg.substring(msg.length() - 17);
                    String name = msg.substring(0, msg.length() - 18);
                    //lv2.setSelection(arg2);
                    btdialog.cancel();
                    adrress.setText(mac);
                    Log.i("TAG", "mac=" + mac);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    /*
    找可连接的蓝牙设备
     */
    private void findAvalibleDevice() {
        // TODO Auto-generated method stub
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device = bluetoothAdapter.getBondedDevices();

        btList.clear();
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            BtBoudAdapter.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //存在已经配对过的蓝牙设备
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                btList.add(btd.getName() + '\n' + btd.getAddress());
                BtBoudAdapter.notifyDataSetChanged();
            }
        } else {  //不存在已经配对过的蓝牙设备
            btList.add("No can be matched to use bluetooth");
            BtBoudAdapter.notifyDataSetChanged();
        }

    }


    View dialogView3;
    private TextView tv_usb;
    private List<String> usbList, usblist;
    private ListView lv_usb;
    private ArrayAdapter<String> adapter3;

    /*
    uSB连接
     */
    private void setUSB() {
        LayoutInflater inflater = LayoutInflater.from(this);
        dialogView3 = inflater.inflate(R.layout.usb_link, null);
        tv_usb = (TextView) dialogView3.findViewById(R.id.textView1);
        lv_usb = (ListView) dialogView3.findViewById(R.id.listView1);


        usbList = PosPrinterDev.GetUsbPathNames(this);
        if (usbList == null) {
            usbList = new ArrayList<>();
        }
        usblist = usbList;
        tv_usb.setText(getString(R.string.usb_pre_con) + usbList.size());
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usbList);
        lv_usb.setAdapter(adapter3);


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView3).create();
        dialog.show();

        setUsbLisener(dialog);

    }

    String usbDev = "";

    public void setUsbLisener(final AlertDialog dialog) {

        lv_usb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                usbDev = usbList.get(i);
                adrress.setText(usbDev);
                dialog.cancel();
                Log.e("usbDev: ", usbDev);
            }
        });
    }

    /*********************-- MainActivity.[Utils] --*********************/

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRESER_IMG_OK:
                    String data = (String) msg.obj;
                    preserData(data);

                    break;
            }
        }
    };

    private void preserData(String data) {
        Log.e("MainActivity", "MainActivity preserData()" + data);

        Gson gson = new Gson();
        WordInfo wordInfo = gson.fromJson(data, WordInfo.class);

        if (wordInfo.getError_code() != null) {
            if (wordInfo.getError_code() == 17 || wordInfo.getError_code() == 19 || wordInfo.getError_code() == 18) {
                Toast.makeText(MyApp.getContext(), "请求量超出限额", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        List<WordInfo.WordsResultBean> words_result = wordInfo.getWords_result();
        Log.e("MainActivity", "MainActivity preserData()" + wordInfo.getWords_result());

        if (wordInfo.getWords_result() == null || wordInfo.getWords_result_num() < 0 || wordInfo.getWords_result().size() == 0) {
            Toast.makeText(MyApp.getContext(), "文字扫描识别失败，请重试", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();


        for (WordInfo.WordsResultBean wordsResultBean : words_result) {
            sb.append(wordsResultBean.getWords());
        }

        tv_car_no.setText(sb.toString().toUpperCase());
    }

    /**
     * 打开相机请求码
     */
    private static final int OPEN_CAMERA_OK = 100;

    public void openCameraByBaidu() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, OPEN_CAMERA_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断请求码是否是请求打开相机的那个请求码
        if (requestCode == OPEN_CAMERA_OK && resultCode == RESULT_OK) {

            String photoPath = FileUtil.getSaveFile(this).getAbsolutePath();
            checkData(photoPath);
        }
    }

    /**
     * 解析图片文字成功 Handler使用
     */
    private static final int PRESER_IMG_OK = 101;

    /**
     * 请求百度API接口，进行获取数据
     *
     * @param filePath
     */
    private void checkData(String filePath) {
        logger.i("MainActivity.checkData", "********************-- MainActivity.checkData[start] --********************");
        try {

            //把图片文件转换为字节数组
            byte[] imgData = FileUtil.readFileByBytes(filePath);

            //对字节数组进行Base64编码
            String imgStr = Base64Util.encode(imgData);
            final String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");

            RequestParams entiry = new RequestParams(ConstantValue.BAIDU_TOKEN_URL);

            x.http().get(entiry, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(final String result) {
                    Gson gson = new Gson();
                    TokenInfo tokenInfo = gson.fromJson(result, TokenInfo.class);

                    final String access_token = tokenInfo.getAccess_token();
                    new Thread() {
                        public void run() {
                            String resultStr = HttpUtil.post(ConstantValue.BAIDU_INTER_URL, access_token, params);
                            Log.e("MainActivity", "MainActivity onSuccess()" + resultStr);
                            Message msg = Message.obtain();
                            msg.obj = resultStr;
                            msg.what = PRESER_IMG_OK;
                            handler.sendMessage(msg);
                        }
                    }.start();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("MainActivity", "onError: " + ex);

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.i("MainActivity.checkData", "********************-- MainActivity.checkData[end] --********************");
    }

}
