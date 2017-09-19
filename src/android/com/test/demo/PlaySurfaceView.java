package com.test.demo;

import org.MediaPlayer.PlayM4.Player;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;

@SuppressLint("NewApi")
public class PlaySurfaceView extends SurfaceView implements Callback {
	
	private final String 	TAG						= "PlaySurfaceView";
	private int m_iWidth = 0;
	private int m_iHeight = 0;
	public int m_iPreviewHandle = -1;
	private int m_iPort = -1;
	private boolean m_bSurfaceCreated = false;

	public PlaySurfaceView(DemoActivity demoActivity) {
		super((Context) demoActivity);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		System.out.println("surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		m_bSurfaceCreated = true;
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		if (-1 == m_iPort)
		{
			return;
		}
        Surface surface = arg0.getSurface();
        if (true == surface.isValid()) {
        	if (false == Player.getInstance().setVideoWindow(m_iPort, 0, arg0)) {	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}	
    	}      
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		m_bSurfaceCreated = false;
		if (-1 == m_iPort)
		{
			return;
		}
        if (true == arg0.getSurface().isValid()) 
        {
        	if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) 
        	{	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}
        }
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.setMeasuredDimension(m_iWidth - 1, m_iHeight - 1);
    }
	
	public void setParam(int nScreenSize)
	{
		m_iWidth = nScreenSize / 2;
		m_iHeight = (m_iWidth * 3) / 4;
	}
	
	public int getCurWidth()
	{
		return m_iWidth;
	}
	public int getCurHeight()
	{
		return m_iHeight;
	}
	
	public void startPreview(int iUserID, int iChan)
	{
		RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
		if (fRealDataCallBack == null)
		{
		    Log.e(TAG, "fRealDataCallBack object is failed!");
            return ;
		}
		Log.i(TAG, "preview channel:" +iChan);
		        
        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = iChan;
        previewInfo.dwStreamType = 1; //substream
        previewInfo.bBlocked = 1;       
		// HCNetSDK start preview
        m_iPreviewHandle = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(iUserID, previewInfo, fRealDataCallBack);
		if (m_iPreviewHandle < 0)
		{
		 	Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}
	public void stopPreview()
	{
		HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPreviewHandle);
		stopPlayer();
	}
	private void stopPlayer()
	{
		Player.getInstance().stopSound();
		// player stop play
		if (!Player.getInstance().stop(m_iPort)) 
        {
            Log.e(TAG, "stop is failed!");
            return;
        }	
		
		if(!Player.getInstance().closeStream(m_iPort))
		{
            Log.e(TAG, "closeStream is failed!");
            return;
        }		
		if(!Player.getInstance().freePort(m_iPort))
		{
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
		m_iPort = -1;
	}
	
	private RealPlayCallBack getRealPlayerCbf()
	{
	    RealPlayCallBack cbf = new RealPlayCallBack()
        {
             public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize)
             {
            	processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME); 
             }
        };
        return cbf;
	}
	
	private void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode)
	{
	 //   Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
	    if(HCNetSDK.NET_DVR_SYSHEAD == iDataType)
	    {
	    	if(m_iPort >= 0)
    		{
    			return;
    		}	    			
    		m_iPort = Player.getInstance().getPort();	
    		if(m_iPort == -1)
    		{
    			Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
    			return;
    		}
    		Log.i(TAG, "getPort succ with: " + m_iPort);
    		if (iDataSize > 0)
    		{
    			if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode))  //set stream mode
    			{
    				Log.e(TAG, "setStreamOpenMode failed");
    				return;
    			}
    			if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2*1024*1024)) //open stream
    			{
    				Log.e(TAG, "openStream failed");
    				return;
    			}
    			while(!m_bSurfaceCreated)
    			{
    				try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				Log.i(TAG, "wait 100 for surface, handle:" + iPlayViewNo);
    			}
    			
    			if (!Player.getInstance().play(m_iPort, getHolder())) 
    			{
    				Log.e(TAG, "play failed,error:" + Player.getInstance().getLastError(m_iPort));
    				return;
    			}	
    			if(!Player.getInstance().playSound(m_iPort))
				{
					Log.e(TAG, "playSound failed with error code:" + Player.getInstance().getLastError(m_iPort));
					return;
				}
    		}
	    }
	    else
	    {
	    	if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize))
    		{
    			Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
    		}	 
	    }
	}
}
