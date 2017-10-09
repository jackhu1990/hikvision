//
//  SimpleDemoViewController.h
//  SimpleDemo
//
//  Created by apple on 11-4-2.
//  Copyright __MyCompanyName__ 2011. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DeviceInfo.h"

#define RTP

@interface SimpleDemoViewController : UIViewController 
{
	IBOutlet UITextField	*m_deviceIpField;
	IBOutlet UITextField	*m_devicePortField;
	IBOutlet UITextField	*m_uerNameField;
	IBOutlet UITextField	*m_passwordField;
	
	IBOutlet UIButton		*m_playButton;	
	IBOutlet UIButton		*m_playbackButton;	
    IBOutlet UIButton       *m_loginButton;
    IBOutlet UIButton       *m_getcfgButton;
    IBOutlet UIButton       *m_captureButton;
    IBOutlet UIButton       *m_recordButton;
    IBOutlet UIButton       *m_talkButton;
    IBOutlet UIButton       *m_ptzButton;
    IBOutlet UIButton       *m_otherButton;
	
	UIView                  *m_playView;
    UIView                  *m_multiView[4];
	
	id                      m_playThreadID;
	bool					m_bThreadRun;
	int                     m_lUserID;
	int                     m_lRealPlayID;
    int                     m_lPlaybackID;
    bool                    m_bPreview;
    bool                    m_bRecord;
    bool                    m_bPTZL;
    bool                    m_bVoiceTalk;
    bool                    m_bStopPlayback;
    
    NSString* username;
    NSString* password;
    NSString* ip;
    NSString* port;
    NSString* channel;
    }

@property (nonatomic, retain) IBOutlet UITextField	*m_deviceIpField;
@property (nonatomic, retain) IBOutlet UITextField	*m_devicePortField;
@property (nonatomic, retain) IBOutlet UITextField	*m_uerNameField;
@property (nonatomic, retain) IBOutlet UITextField	*m_passwordField;
@property (nonatomic, retain) IBOutlet UIView	    *m_playView;

@property (nonatomic, retain) IBOutlet UIButton		*m_playButton;
@property (nonatomic, retain) IBOutlet UIButton		*m_playbackButton;
@property (nonatomic, retain) IBOutlet UIButton		*m_loginButton;
@property (nonatomic, retain) IBOutlet UIButton     *m_getcfgButton;
@property (nonatomic, retain) IBOutlet UIButton     *m_captureButton;
@property (nonatomic, retain) IBOutlet UIButton     *m_recordButton;
@property (nonatomic, retain) IBOutlet UIButton     *m_talkButton;
@property (nonatomic, retain) IBOutlet UIButton     *m_ptzButton;
@property (nonatomic, retain) IBOutlet UIButton     *m_otherButton;

@property (nonatomic, retain) id m_playThreadID;
@property bool m_bThreadRun;
@property int m_lUserID;
@property int m_lRealPlayID;
@property int m_lPlaybackID;
@property bool m_bPreview;
@property bool m_bRecord;
@property bool m_bPTZL;
@property bool m_bVoiceTalk;
@property bool m_bStopPlayback;

-(IBAction) playerBtnClicked:(id)sender;
-(IBAction) playbackBtnClicked:(id)sender;
-(IBAction) loginBtnClicked:(id)sender;
-(IBAction) getcfgBtnClicked:(id)sender;
-(IBAction) captureBtnClicked:(id)sender;
-(IBAction) recordBtnClicked:(id)sender;
-(IBAction) talkBtnClicked:(id)sender;
-(IBAction) ptzBtnClicked:(id)sender;
-(IBAction) ptzBtnClickedUp:(id)sender;
-(IBAction) otherBtnClicked:(id)sender;
-(IBAction) backClicked:(id)sender;

// enter/exit edit box
- (IBAction) textFieldEditingDidBegin:(id)sender;
- (IBAction) textFieldEditingDidEndOnExit:(id)sender;

// hide keyboard
- (IBAction) keyboardWillHide:(NSNotification *)note;

- (bool) validateValue:(DeviceInfo*)deviceInfo;
- (bool) isValidIP:(NSString *)ipStr;
- (void) startPlay;
- (void) stopPlay;
- (void) startPlayback;
- (void) stopPlayback;

- (bool) loginNormalDevice;
- (bool) loginEZVIZDevice;

- (void) setCommand:(NSString*) _username:(NSString*) _password:(NSString*) _ip:(NSString*) _port:(NSString*) _channel;
@end
