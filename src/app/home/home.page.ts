import { Component } from '@angular/core';
import { IonicModule, Platform } from '@ionic/angular';
import MyStatusBar from '../myPlugin/StatusBar';
import { ThermalPrinterPlugin } from 'thermal-printer-cordova-plugin/src';
declare let ThermalPrinter: ThermalPrinterPlugin;

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule],
})
export class HomePage {
  constructor(private platform: Platform) {
    this.platform.ready().then(() => {
      this.getStatusBarHeight();
    })
  }

  printBT() {
    // Now that Bluetooth is enabled, proceed with printing
    ThermalPrinter.printFormattedText({
      type: 'bluetooth',
      id: 'first', // You can also use the identifier directly i.e., 00:11:22:33:44:55 (address) or name
      text: "[C]<u><font size='big'>Hello World</font></u>\n" // Note the "\n" for new lines
    }, () => {
      console.log('Successfully printed!');
    }, (error) => {
      console.error('Printing error', error);
    });
  }

  viberation() {
    const kranthi = MyStatusBar.getVibration();
  }

  MobileSettings(){
    const kranthi = MyStatusBar.getMobileSettings();
  }

  async getStatusBarHeight(){
    try{
      const kranthi = await MyStatusBar.getHeight();
      console.log("22222222222222222222222222222",kranthi.height);
    
    }catch(error){
      console.log("1111111111111111111111", error);
    }
  }



}
