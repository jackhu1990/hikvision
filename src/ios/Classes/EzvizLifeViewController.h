//
//  EzvizLifeViewController.h
//  SimpleDemo
//
//  Created by Netsdk on 17/9/1.
//
//

#import <UIKit/UIKit.h>

typedef void (^returnBlock)(NSString *showText);

@interface EzvizLifeViewController : UIViewController

@property (nonatomic, copy)returnBlock returnTextBlock;

@property (retain, nonatomic) IBOutlet UIWebView *webview;

@end
