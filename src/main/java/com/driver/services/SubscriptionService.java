package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        //Amount Calculation
        int amount=0;
        if (subscription.getSubscriptionType().toString().equals("BASIC"))
        {
            amount = 500+(200*subscriptionEntryDto.getNoOfScreensRequired());
        }
        else if (subscription.getSubscriptionType().toString().equals("PRO"))
        {
            amount = 800+(250*subscriptionEntryDto.getNoOfScreensRequired());
        }
        else
        {
            amount = 1000+(350*subscriptionEntryDto.getNoOfScreensRequired());
        }
        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user =  userRepository.findById(userId).get();
        Subscription userSubscription = user.getSubscription();
        SubscriptionType subscriptionType = user.getSubscription().getSubscriptionType();

        int previousAmount = user.getSubscription().getTotalAmountPaid();
        int currentAmount=0;

        if (subscriptionType.toString().equals("BASIC"))
        {
            //upgrading to PRO
            currentAmount= 800+(250*user.getSubscription().getNoOfScreensSubscribed());
            userSubscription.setSubscriptionType(subscriptionType.PRO);
        }
        else if (subscriptionType.toString().equals("PRO"))
        {
            //upgrading to ELITE
            currentAmount= 1000+(350*user.getSubscription().getNoOfScreensSubscribed());
            userSubscription.setSubscriptionType(subscriptionType.ELITE);
        }
        return (currentAmount-previousAmount);
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int totalRevenue=0;
        for(Subscription subscription: subscriptionList)
        {
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
