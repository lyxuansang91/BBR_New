package com.cadang.android.bbrproject.util;


public class ExampleStream {
    public static final String strTest01Stream	= "http://10.0.0.101:8080/sway.mp3";
    public static final String strTest02Stream	= "http://10.0.0.101:8080/Ringer1.mp3";
    public static final String strTest03Stream	= "http://10.0.0.101:8080/Test03.mp3";
    public static final String strTest04Stream	= "http://10.0.0.101:8080/lalala.mp3";
 
    public static final String strTest01Label	= "Localhost Test - Mp3 - Sway";
    public static final String strTest02Label	= "Local Test - Ringer1";
    public static final String strTest03Label	= "Local Test - Test 03";
    public static final String strTest04Label	= "Local Test - La la la";
    public static final MediaStream[] MEDIASTREAMS =
        {
            new MediaStream(strTest01Label, strTest01Stream),
            new MediaStream(strTest02Label, strTest02Stream),
            new MediaStream(strTest03Label, strTest03Stream),
            new MediaStream(strTest04Label, strTest04Stream)
        };
    public static final MediaStream DEFAULT_STREAM_MEDIA = MEDIASTREAMS[0];
}