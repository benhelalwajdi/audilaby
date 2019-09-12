package com.unity3d.ads.unity;

import com.unity3d.ads.IUnityAdsListener;

public interface IUnityAdsUnityListener extends IUnityAdsListener {

  /**
   * Called when an in-app purchase is initiated from an ad.
   *
   * @param eventString The string provided via the ad.
   */
  void onUnityAdsInitiatePurchase(String eventString);

}
