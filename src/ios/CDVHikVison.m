#import "CDVHikVison.h"
#import "Classes/SimpleDemoViewController.h"

@implementation CDVHikVison

-(void)startMonitor:(CDVInvokedUrlCommand *)command{
    if (command.arguments.count>0) {
        // username, password, ip, port, channel
        NSString* username = command.arguments[0];
        NSString* password = command.arguments[1];
        NSString* ip = command.arguments[2];
        NSString* port = command.arguments[3];
        NSString* channel = command.arguments[4];
         SimpleDemoViewController* testViewCtrl = [[SimpleDemoViewController alloc]init];
        [self.viewController presentViewController:testViewCtrl animated:YES completion:^{
        }];
    }
}

@end