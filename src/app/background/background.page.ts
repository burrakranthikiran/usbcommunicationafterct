import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Plugins } from '@capacitor/core';
import 'contacts-plugin'
const {ContactsPlugin} = Plugins;




@Component({
  selector: 'app-background',
  templateUrl: './background.page.html',
  styleUrls: ['./background.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class BackgroundPage implements OnInit {
  device_responce = 'Device ID: ' + 'null';
  contact = [];

  constructor() { }

  async loadContacts(){
    console.log('ContactsPlugin:', ContactsPlugin);
    console.log('getContacts:', ContactsPlugin['getContacts']); // Add this log statement
    console.log('Loading contacts...');
    // this.contact = ContactsPlugin['getContacts']().results;

    try {
      const response = await ContactsPlugin['getContacts']('whatever filter');
      if (response && response.results) {
        this.contact = response.results;
        this.device_responce = JSON.stringify(this.contact); 
      } else {
        // Handle the case where the response is not as expected
        console.error('Unexpected response:', JSON.stringify(response));
        this.device_responce = JSON.stringify(response); 
      }
    } catch (error) {
      // Handle any errors that may occur during the contact retrieval
      console.error('Error:', error);
    }


    // this.contact = (await ContactsPlugin['getContacts']('whatever filter')).results;
    // console.log("333333333333333333333333333333333333",JSON.stringify(this.contact));
    // this.device_responce = JSON.stringify(this.contact); 
  }
  

  ngOnInit() {
  }

}
