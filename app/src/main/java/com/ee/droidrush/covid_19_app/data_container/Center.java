package com.ee.droidrush.covid_19_app.data_container;

public class Center {
       public final int dose1;
       public final int dose2;
       int center_id;
       public String name,address,state_name,district_name,block_name,pincode,lat,longi,from,to,fee_type,session_id,date,available_capacity,min_age_limit,allow_all_age ,vaccine ,available_capacity_dose1,available_capacity_dose2;
       public Center(String name,String address,String vaccine,String fee_type,String min_age_limit,int dose1,int dose2)
       {
              this.name=name;
              this.address=address;
              this.vaccine=vaccine;
              this.fee_type=fee_type;
              this.min_age_limit=min_age_limit;
              this.dose1=dose1;
              this.dose2=dose2;
       }

}
