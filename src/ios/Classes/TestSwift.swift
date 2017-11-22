//
//  TestSwift.swift
//  SimpleDemo
//
//  Created by Netsdk on 17/9/11.
//
//

import Foundation

@objc(HikSwift)

class HikSwift: NSObject {
    var obj = OcObj();
    var devInfo = OC_NET_DVR_DEVICEINFO_V30();
    var lUserID = Int32();
    var previewInfo = OC_NET_DVR_PREVIEWINFO();
    
    public func Test_Login() -> Int32{
        lUserID = obj.net_DVR_Login_V30("10.17.133.46", wDVRPort:8000, sUserName:"admin", sPassword:"hik12345", oc_LPNET_DVR_DEVICEINFO_V30: devInfo);
        return lUserID;
    }
    
    public func Test_Logout(lUserID:Int32) -> Bool{
        return obj.net_DVR_Logout(lUserID);
    }
    
    public func Test_RealPlay(hPlayWnd:UnsafeMutableRawPointer) -> Int32{
        previewInfo.lChannel = 1;
        previewInfo.dwStreamType = 0;
        previewInfo.bBlocked = 0;
        previewInfo.hPlayWnd = hPlayWnd;
        return obj.net_DVR_RealPlay_V40(lUserID, lpPreviewInfo: previewInfo);
    }
    
    
    public func Test_StopRealPlay(lRealHandle:Int32) -> Bool{
        return obj.net_DVR_StopRealPlay(lRealHandle);
    }
}
