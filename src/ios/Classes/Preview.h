//
//  Preview.h
//  SimpleDemo
//
//  Created by Netsdk on 15/4/22.
//
//

#ifndef SimpleDemo_Preview_h
#define SimpleDemo_Preview_h
#import "SimpleDemoViewController.h"

#define MAX_VIEW_NUM    4


int startPreview(int iUserID, int iStartChan, UIView *pView, int iIndex);
void stopPreview(int iIndex);

#endif
