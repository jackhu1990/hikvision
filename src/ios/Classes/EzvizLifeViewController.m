//
//  EzvizLifeViewController.m
//  SimpleDemo
//
//  Created by Netsdk on 17/9/1.
//
//

#import "EzvizLifeViewController.h"

@interface EzvizLifeViewController ()

@end

@implementation EzvizLifeViewController
@synthesize webview;

BOOL authed = false;
NSString *m_retUrl;
NSURL *url;
NSURLRequest* request;


- (void)viewDidLoad {
    [super viewDidLoad];

    
    url = [NSURL URLWithString:@"https://openauth.ezvizlife.com/oauth/ddns/56bdd1135f9a11e7ae26fa163e8bac01?areaId=248"];
    
    request = [NSURLRequest requestWithURL:url];
    
    [webview loadRequest:request];
 
}

-(void)connection:(NSURLConnection *)connection willSendRequestForAuthenticationChallenge:( NSURLAuthenticationChallenge *)challenge
{
    if([challenge previousFailureCount] == 0)
    {
        authed = YES;
        NSURLCredential* cre = [NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust];
        [challenge.sender useCredential:cre forAuthenticationChallenge:challenge];
    }
}


#pragma mark -
#pragma mark - method

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = NO;
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:YES];
    
    m_retUrl = [webview stringByEvaluatingJavaScriptFromString:@"document.location.href"];
//    m_retUrl = webview.request.URL.absoluteString;
    
    if(self.returnTextBlock != nil){
        self.returnTextBlock(m_retUrl);
    }
    self.navigationController.navigationBarHidden = YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [webview release];
    [super dealloc];
}


@end
