//
//  Preview.m
//  SimpleDemo
//
//  Created by Netsdk on 15/4/22.
//
//

#import <Foundation/Foundation.h>
#import "Preview.h"
#import "hcnetsdk.h"

int g_iPreviewHandle[MAX_VIEW_NUM];

int startPreview(int iUserID, int iStartChan, UIView *pView, int iIndex)
{
    // request stream
    NET_DVR_PREVIEWINFO struPreviewInfo = {0};
    struPreviewInfo.lChannel = iStartChan + iIndex;
    struPreviewInfo.dwStreamType = 1;
    struPreviewInfo.bBlocked = 1;
    struPreviewInfo.hPlayWnd = pView;
    g_iPreviewHandle[iIndex] = NET_DVR_RealPlay_V40(iUserID, &struPreviewInfo, NULL, NULL);
    if (g_iPreviewHandle[iIndex] == -1)
    {
        NSLog(@"NET_DVR_RealPlay_V40 failed:%d",  NET_DVR_GetLastError());
        return -1;
    }
    NSLog(@"NET_DVR_RealPlay_V40 succ");
    
    return g_iPreviewHandle[iIndex];
}

void stopPreview(int iIndex)
{
    NET_DVR_StopRealPlay(g_iPreviewHandle[iIndex]);
}
