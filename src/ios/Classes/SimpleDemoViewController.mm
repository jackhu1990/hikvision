//
//  SimpleDemoViewController.m
//  SimpleDemo
//
//  Created by apple on 11-4-2.
//  Copyright __MyCompanyName__ 2011. All rights reserved.
//
#import "SimpleDemoViewController.h"
#import "hcnetsdk.h"
#import "HikDec.h"
#import "OtherTest.h"
#import "VoiceTalk.h"
#import "Preview.h"
#import "EzvizTrans.h"
#import <Foundation/Foundation.h>
#include <stdio.h>
#include <ifaddrs.h>
#include <sys/socket.h>
#include <sys/poll.h>
#include <net/if.h>
#include <map>

@implementation SimpleDemoViewController

@synthesize m_deviceIpField;
@synthesize m_devicePortField;
@synthesize m_uerNameField;
@synthesize m_passwordField;
@synthesize m_playView;

@synthesize m_playButton;
@synthesize m_playbackButton;
@synthesize m_loginButton;
@synthesize m_getcfgButton;
@synthesize m_captureButton;
@synthesize m_recordButton;
@synthesize m_talkButton;
@synthesize m_ptzButton;
@synthesize m_otherButton;

@synthesize m_lUserID;
@synthesize m_lRealPlayID;
@synthesize m_lPlaybackID;
@synthesize m_bPreview;
@synthesize m_bRecord;
@synthesize m_bPTZL;
@synthesize m_bVoiceTalk;
@synthesize m_bStopPlayback;

SimpleDemoViewController *g_pController = NULL;

int g_iStartChan = 0;
int g_iPreviewChanNum = 0;

void g_fExceptionCallBack(DWORD dwType, LONG lUserID, LONG lHandle, void *pUser) {
    NSLog(@"g_fExceptionCallBack Type[0x%x], UserID[%d], Handle[%d]", dwType, lUserID, lHandle);
}

//other function button click up
- (IBAction)otherBtnClicked:(id)sender {
    TEST_Manage(m_lUserID, g_iStartChan);
//    TEST_PTZ(m_lRealPlayID, m_lUserID, g_iStartChan);
//    TEST_Config(m_lRealPlayID, m_lUserID, g_iStartChan);
//    TEST_Other(m_lRealPlayID, m_lUserID, g_iStartChan, m_lPlaybackID);
//    TEST_Alarm(m_lUserID);

}

//ptz button click up
- (IBAction)ptzBtnClickedUp:(id)sender {
    NSLog(@"ptzBtnClickedUp");
    if (m_bPTZL == true) {
        if (!NET_DVR_PTZControl_Other(m_lUserID, g_iStartChan, PAN_LEFT, 1)) {
            NSLog(@"stop PAN_LEFT failed with[%d]", NET_DVR_GetLastError());
        } else {
            NSLog(@"stop PAN_LEFT succ");
        }
        [m_ptzButton setTitle:@"PTZ(R)" forState:UIControlStateNormal];
    } else {
        if (!NET_DVR_PTZControl_Other(m_lUserID, g_iStartChan, PAN_RIGHT, 1)) {
            NSLog(@"stop PAN_RIGHT failed with[%d]", NET_DVR_GetLastError());
        } else {
            NSLog(@"stop PAN_RIGHT succ");
        }
        [m_ptzButton setTitle:@"PTZ(L)" forState:UIControlStateNormal];
    }
}

//ptz button click
- (IBAction)ptzBtnClicked:(id)sender {
    NSLog(@"ptzBtnClicked");
    if (m_lUserID < 0) {
        NSLog(@"Please logon a device first!");
        return;
    }
    if (m_bPTZL == false) {
        if (!NET_DVR_PTZControl_Other(m_lUserID, g_iStartChan, PAN_LEFT, 0)) {
            NSLog(@"start PAN_LEFT failed with[%d]", NET_DVR_GetLastError());
        } else {
            NSLog(@"start PAN_LEFT succ");
        }
        m_bPTZL = true;
    } else {
        if (!NET_DVR_PTZControl_Other(m_lUserID, g_iStartChan, PAN_RIGHT, 0)) {
            NSLog(@"start PAN_RIGHT failed with[%d]", NET_DVR_GetLastError());
        } else {
            NSLog(@"start PAN_RIGHT succ");
        }
        m_bPTZL = false;
    }
}

//talk button click
- (IBAction)talkBtnClicked:(id)sender {
    NSLog(@"talkBtnClicked");
#if !TARGET_IPHONE_SIMULATOR
    if (!m_bVoiceTalk) {
        if (startVoiceTalk(m_lUserID) >= 0) {
            m_bVoiceTalk = true;
        }
    } else {
        stopVoiceTalk();
        m_bVoiceTalk = false;
    }
#endif
}

// record button click while realplay
- (IBAction)recordBtnClicked:(id)sender {
    NSLog(@"recordBtnClicked");
    if (m_bRecord == false) {
        if (m_lRealPlayID < 0) {
            NSLog(@"Please start realplay first!");
            return;
        }

        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        char szFileName[256] = {0};
        NSString *date;
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"YYYY-MM-dd-hh-mm-ss"];
        date = [formatter stringFromDate:[NSDate date]];
        sprintf(szFileName, "%s/%s.mp4", (char *) documentsDirectory.UTF8String, (char *) date.UTF8String);
        if (!NET_DVR_SaveRealData(m_lRealPlayID, szFileName)) {
            NSLog(@"NET_DVR_SaveRealData failed with[%d]", NET_DVR_GetLastError());
            return;
        }
        NSLog(@"NET_DVR_SaveRealData succ [%s]", szFileName);

        m_bRecord = true;
        [m_recordButton setTitle:@"Stop Record" forState:UIControlStateNormal];
    } else {
        NET_DVR_StopSaveRealData(m_lRealPlayID);
        m_bRecord = false;
        [m_recordButton setTitle:@"Start Record" forState:UIControlStateNormal];
    }
}

// capture button click
- (IBAction)captureBtnClicked:(id)sender {
    NSLog(@"captureBtnClicked");
    if (m_lRealPlayID < 0) {
        NSLog(@"Please start realplay first!");
        return;
    }

    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    char szFileName[256] = {0};
    NSString *date;
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"YYYY-MM-dd hh:mm:ss"];
    date = [formatter stringFromDate:[NSDate date]];

    sprintf(szFileName, "%s/%s.bmp", (char *) documentsDirectory.UTF8String, (char *) date.UTF8String);

    if (NET_DVR_CapturePictureBlock(m_lRealPlayID, szFileName, 0)) {
        NSLog(@"NET_DVR_CapturePicture succ[%s]", szFileName);
    } else {
        NSLog(@"NET_DVR_CapturePicture failed[%d]", NET_DVR_GetLastError());
    }

    return;
}

// preview button Click
- (IBAction) playerBtnClicked:(id)sender {
    NSLog(@"liveStreamBtnClicked");
    g_iStartChan = [channel intValue];
    g_iPreviewChanNum = 0;
    if (g_iPreviewChanNum > 1) {
        if (!m_bPreview) {
            int iPreviewID[MAX_VIEW_NUM] = {0};
            for (int i = 0; i < MAX_VIEW_NUM; i++) {
                iPreviewID[i] = startPreview(m_lUserID, g_iStartChan, m_multiView[i], i);
            }
            m_lRealPlayID = iPreviewID[0];
            m_bPreview = true;
            [m_playButton setTitle:@"Stop Preview" forState:UIControlStateNormal];
        } else {
            for (int i = 0; i < MAX_VIEW_NUM; i++) {
                stopPreview(i);
            }
            m_bPreview = false;
            [m_playButton setTitle:@"Start Preview" forState:UIControlStateNormal];
        }
    } else {
        if (!m_bPreview) {
            m_lRealPlayID = startPreview(m_lUserID, g_iStartChan, m_playView, 0);
            if (m_lRealPlayID >= 0) {
                m_bPreview = true;
                [m_playButton setTitle:@"Stop Preview" forState:UIControlStateNormal];
            }
        } else {
            stopPreview(0);
            m_bPreview = false;
            [m_playButton setTitle:@"Start Preview" forState:UIControlStateNormal];
        }
    }
}

//config button click
- (IBAction) getcfgBtnClicked:(id)sender {
    NSLog(@"getcfgBtnClicked");

    if (m_lUserID == -1) {
        NSLog(@"Please logon a device first!");
        return;
    }

    NET_DVR_COMPRESSIONCFG_V30 struCompress = {0};
    DWORD dwRet = 0;
    if (!NET_DVR_GetDVRConfig(m_lUserID, NET_DVR_GET_COMPRESSCFG_V30, g_iStartChan, &struCompress, sizeof(struCompress), &dwRet)) {
        NSLog(@"NET_DVR_GET_COMPRESSCFG_V30 failed with[%d]", NET_DVR_GetLastError());
    } else {
        NSLog(@"NET_DVR_GET_COMPRESSCFG_V30 succ");
    }
    //set substream resolution to cif
    struCompress.struNetPara.byResolution = 1;
    if (!NET_DVR_SetDVRConfig(m_lUserID, NET_DVR_SET_COMPRESSCFG_V30, g_iStartChan, &struCompress, sizeof(struCompress))) {
        NSLog(@"NET_DVR_SET_COMPRESSCFG_V30 failed with[%d]", NET_DVR_GetLastError());
    } else {
        NSLog(@"NET_DVR_SET_COMPRESSCFG_V30 succ");
    }
}

//start player
- (void)startPlayer {
    [self performSelectorOnMainThread:@selector(playerPlay)
                           withObject:nil
                        waitUntilDone:NO];
}


// playback button click (bytime)
- (IBAction) playbackBtnClicked:(id)sender {
    NSLog(@"playbackBtnClicked");
    
}

/*
 // playback button click (by Name)
-(IBAction) playbackBtnClicked:(id)sender
{
    NSLog(@"playbackBtnClicked");
    if (m_lPlaybackID == -1)
    {
        if(m_lUserID == -1)
        {
            NSLog(@"Please login on the device first!");
            return;
        }
        
        m_lPlaybackID = NET_DVR_PlayBackByName(m_lUserID, "ch0001_00010000091000000", m_playView);
        if (m_lPlaybackID == -1)
        {
            NSLog(@"NET_DVR_PlayBackByName failed:%d",  NET_DVR_GetLastError());
            UIAlertView *alert = [[UIAlertView alloc]
                                  initWithTitle:kWarningTitle
                                  message:kRealPlayFailMsg
                                  delegate:nil
                                  cancelButtonTitle:kWarningConfirmButton
                                  otherButtonTitles:nil];
            [alert show];
            [alert release];
            
            [self stopPlayback];
            return;
        }
        
        if (!NET_DVR_PlayBackControl_V40(m_lPlaybackID, NET_DVR_PLAYSTART, NULL, 0, NULL, NULL))
        {
            NSLog(@"NET_DVR_PlayBackControl_V40 failed:%d",  NET_DVR_GetLastError());
            [self stopPlayback];
            return;
        }
        m_bStopPlayback = false;
        [m_playbackButton setTitle:@"Stop Playback" forState:UIControlStateNormal];
    }
    else
    {
        m_bStopPlayback = true;
        [self stopPlayback];
        [m_playbackButton setTitle:@"Start Playback" forState:UIControlStateNormal];
    }
}
*/
- (bool)loginNormalDevice {
    //  Get value
    NSString *iP = m_deviceIpField.text;
    NSString *port = m_devicePortField.text;
    NSString *usrName = m_uerNameField.text;
    NSString *password = m_passwordField.text;

    DeviceInfo *deviceInfo = [[DeviceInfo alloc] init];
    deviceInfo.chDeviceAddr = iP;
    deviceInfo.nDevicePort = [port integerValue];
    deviceInfo.chLoginName = usrName;
    deviceInfo.chPassWord = password;

    // device login
    NET_DVR_DEVICEINFO_V30 logindeviceInfo = {0};

    // encode type
    NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    m_lUserID = NET_DVR_Login_V30((char *) [deviceInfo.chDeviceAddr UTF8String],
            deviceInfo.nDevicePort,
            (char *) [deviceInfo.chLoginName cStringUsingEncoding:enc],
            (char *) [deviceInfo.chPassWord UTF8String],
            &logindeviceInfo);

    printf("iP:%s\n", (char *) [deviceInfo.chDeviceAddr UTF8String]);
    printf("Port:%d\n", deviceInfo.nDevicePort);
    printf("UsrName:%s\n", (char *) [deviceInfo.chLoginName cStringUsingEncoding:enc]);
    printf("Password:%s\n", (char *) [deviceInfo.chPassWord UTF8String]);

    // login on failed
    if (m_lUserID == -1) {
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle:kWarningTitle
                              message:kLoginDeviceFailMsg
                              delegate:nil
                              cancelButtonTitle:kWarningConfirmButton
                              otherButtonTitles:nil];
        [alert show];
        //        [alert release];
        return false;
    }

    if (logindeviceInfo.byChanNum > 0) {
        g_iStartChan = logindeviceInfo.byStartChan;
        g_iPreviewChanNum = logindeviceInfo.byChanNum;
    } else if (logindeviceInfo.byIPChanNum > 0) {
        g_iStartChan = logindeviceInfo.byStartDChan;
        g_iPreviewChanNum = logindeviceInfo.byIPChanNum + logindeviceInfo.byHighDChanNum * 256;
    }

    return true;
}

- (bool)loginEZVIZDevice {
    NET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO struLoginInfo = {0};
    NET_DVR_DEVICEINFO_V30 struDeviceInfo = {0};

    sprintf(struLoginInfo.sEzvizServerAddress, "pbdev.ys7.com");
    struLoginInfo.wPort = 443;
    sprintf(struLoginInfo.sAccessToken, "at.bt7wddra6zcmuzs8dgsigxes5118s7ej-7p0aqoy9qr-17h7lx5-63cdp4wrc");
    sprintf(struLoginInfo.sAppID, "com.hik.visualintercom");
    sprintf(struLoginInfo.sFeatureCode, "226f102a99ad0e078504d380b9ddf760");
    sprintf(struLoginInfo.sUrl, "/api/device/transmission");
    sprintf(struLoginInfo.sDeviceID, "111111223");
    sprintf(struLoginInfo.sClientType, "0");
    sprintf(struLoginInfo.sOsVersion, "5.0.1");
    sprintf(struLoginInfo.sNetType, "UNKNOWN");
    sprintf(struLoginInfo.sSdkVersion, "v.5.1.5.30");

    m_lUserID = NET_DVR_CreateOpenEzvizUser(&struLoginInfo, &struDeviceInfo);
    NSLog(@"0000000000000 NET_DVR_CreateEzvizUser[%d] with[%d]", m_lUserID, NET_DVR_GetLastError());

    // login on failed
    if (m_lUserID == -1) {
        DWORD dwRet = -1;
        dwRet = NET_DVR_GetLastError();
        UIAlertView *alert = [[UIAlertView alloc]
                initWithTitle:kWarningTitle
                      message:kLoginDeviceFailMsg
                     delegate:nil
            cancelButtonTitle:kWarningConfirmButton
            otherButtonTitles:nil];
        [alert show];
//        [alert release];
        return false;
    }
    return true;
}
//back button click
- (IBAction)backClicked:(id)sender {
    NSLog(@"backClicked");
   [self dismissViewControllerAnimated:YES completion:nil];
}
// login button click
- (IBAction) loginBtnClicked:(id)sender {
    NSLog(@"loginBtnClicked");

    if (m_lUserID == -1) {
        // init
        BOOL bRet = NET_DVR_Init();
        if (!bRet) {
            NSLog(@"NET_DVR_Init failed");
        }
        NET_DVR_SetExceptionCallBack_V30(0, NULL, g_fExceptionCallBack, NULL);
        NSString *documentPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
        const char *pDir = [documentPath UTF8String];
        NET_DVR_SetLogToFile(3, (char *) pDir, true);

        if ([self loginNormalDevice])
//        if([self loginEZVIZDevice])
        {
            [m_loginButton setTitle:@"Logout" forState:UIControlStateNormal];
        }
    } else {
        NET_DVR_Logout(m_lUserID);
//        NET_DVR_DeleteOpenEzvizUser(m_lUserID);
        NET_DVR_Cleanup();
        m_lUserID = -1;
        [m_loginButton setTitle:@"Login" forState:UIControlStateNormal];
    }
}

//stop preview
- (void)stopPlay {
    if (m_lRealPlayID != -1) {
        NET_DVR_StopRealPlay(m_lRealPlayID);
        m_lRealPlayID = -1;
    }
}

//stop playback
- (void)stopPlayback {
    if (m_lPlaybackID != -1) {
        NET_DVR_StopPlayBack(m_lPlaybackID);
        m_lPlaybackID = -1;
    }
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    m_lUserID = -1;
    m_lRealPlayID = -1;
    m_lPlaybackID = -1;
    m_bRecord = false;
    m_bPTZL = false;

//    m_deviceIpField.text = @"192.168.1.252";
//    m_devicePortField.text = @"8000";
//    m_uerNameField.text = @"admin";
//    m_passwordField.text = @"12345";
    
    m_deviceIpField.text =ip;
    m_devicePortField.text = port;
    m_uerNameField.text = username;
    m_passwordField.text = password;

    int nWidth = m_playView.frame.size.width / 2;
    int nHeight = m_playView.frame.size.height / 2;
    for (int i = 0; i < MAX_VIEW_NUM; i++) {
        m_multiView[i] = [[UIView alloc] initWithFrame:CGRectMake((i % (MAX_VIEW_NUM / 2)) * nWidth, (i / (MAX_VIEW_NUM / 2)) * nHeight, nWidth - 1, nHeight - 1)];
        m_multiView[i].backgroundColor = [UIColor clearColor];
        [m_playView addSubview:m_multiView[i]];
    }


    // hide keybord
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillHide:)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
    g_pController = self;
    [super viewDidLoad];

}
-(void)viewDidDisappear:(BOOL)animated{
    if (m_lRealPlayID != -1) {
        NET_DVR_StopRealPlay(m_lRealPlayID);
        m_lRealPlayID = -1;
    }
    
    if (m_lPlaybackID != -1) {
        NET_DVR_StopPlayBack(m_lPlaybackID);
        m_lPlaybackID = -1;
    }
    
    if (m_lUserID != -1) {
        NET_DVR_Logout(m_lUserID);
        NET_DVR_Cleanup();
        m_lUserID = -1;
    }
}
-(void)viewDidAppear:(BOOL)animated{
    // init
    BOOL bRet = NET_DVR_Init();
    if (!bRet) {
        NSLog(@"NET_DVR_Init failed");
    }
    NET_DVR_SetExceptionCallBack_V30(0, NULL, g_fExceptionCallBack, NULL);
    NSString *documentPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    const char *pDir = [documentPath UTF8String];
    NET_DVR_SetLogToFile(3, (char *) pDir, true);
    if ([self loginNormalDevice])
    {
//        g_iPreviewChanNum = [channel intValue];
//        m_lRealPlayID = startPreview(m_lUserID, g_iStartChan, m_playView, 0);
//        if (m_lRealPlayID >= 0) {
//            m_bPreview = true;
//        }
         [self playerBtnClicked:nil];
    }
}
- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];

    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {

    if (m_lRealPlayID != -1) {
        NET_DVR_StopRealPlay(m_lRealPlayID);
        m_lRealPlayID = -1;
    }

    if (m_lPlaybackID != -1) {
        NET_DVR_StopPlayBack(m_lPlaybackID);
        m_lPlaybackID = -1;
    }

    if (m_lUserID != -1) {
        NET_DVR_Logout(m_lUserID);
        NET_DVR_Cleanup();
        m_lUserID = -1;
    }
}

#pragma mark -
#pragma mark textField UITextField Delegate methods

/*******************************************************************************
 Function:			textFieldEditingDidBegin
 Description:		enter edit box,hide picture,controller up
 Input:				sender �? button down
 Output:			
 Return:			
 *******************************************************************************/
- (IBAction) textFieldEditingDidBegin:(id)sender {
    [UIView beginAnimations:@"login.animation" context:nil];

    [UIView commitAnimations];
}

/*******************************************************************************
 Function:			textFieldEditingDidEndOnExit
 Description:		exit edit box,hide picture,controller focus change
 Input:				sender �? button down
 Output:			
 Return:			
 *******************************************************************************/
- (IBAction) textFieldEditingDidEndOnExit:(id)sender {
    // foucs on username edit box,click done,focus on password edit box
    if (sender == m_deviceIpField) {
        [m_devicePortField becomeFirstResponder];
    } else if (sender == m_devicePortField) {
        [m_uerNameField becomeFirstResponder];
    } else if (sender == m_uerNameField) {
        [m_passwordField becomeFirstResponder];
    }

        // if focus on password edit box,click done,revert GUI
    else if (sender == m_passwordField) {
        [UIView beginAnimations:@"login.animation" context:nil];
    } else {
        // do nothing
    }
}

-(NSUInteger)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskLandscape;
}


/*******************************************************************************
 Function:			keyboardWillHide
 Description:		exit edit box,hide picture,controller focus change
 Input:				note �? keyboard hide
 Output:			
 Return:			
 *******************************************************************************/
- (IBAction)keyboardWillHide:(NSNotification *)note {
    [UIView beginAnimations:@"login.animation" context:nil];
    [UIView commitAnimations];
}

// hide copy and paste button
- (BOOL)canPerformAction:(SEL)action withSender:(id)sender {
    [UIMenuController sharedMenuController].menuVisible = NO;

    return YES;
}

//- (void)dealloc {
//    if (m_playView != nil) {
//        [m_playView release];
//        m_playView = nil;
//    }
//
//    [super dealloc];
//}
- (void) setCommand:(NSString*) _username:(NSString*) _password:(NSString*) _ip:(NSString*) _port:(NSString*) _channel{
    username = _username;
    password = _password;
    ip = _ip;
    port = _port;
    channel = _channel;
}

@end
