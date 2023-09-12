import { Component } from '@angular/core';
import { IonicModule, Platform } from '@ionic/angular';
import MyStatusBar from '../myPlugin/StatusBar';
import { ThermalPrinterPlugin } from 'thermal-printer-cordova-plugin/src';


import EscPosEncoder from 'esc-pos-encoder';
declare let ThermalPrinter: ThermalPrinterPlugin;


@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule],
})
export class HomePage {
  device_status = "Device ID: " + 'null';
  constructor(private platform: Platform) {
    this.platform.ready().then(() => {
      this.getStatusBarHeight();
      this.getStatusPermission();
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

 
// USBPrinter() {
//     console.log("555555555555555555", "working");
//     ThermalPrinter.listPrinters({ type: 'usb' }, function (printers) {
//         if (printers.length > 0) {
//             var printer = {
//                 productName: "USB Portable Printer",
//                 manufacturerName: "STMicroelectronics",
//                 id: 1002,
//                 vendorId: 1110,
//             };

//             let encoder = new EscPosEncoder();

//             let printer_resultsValue = encoder
//                 .initialize()
//                 .text('The quick brown fox jumps over the lazy dog')
//                 .newline()
//                 .encode();
//             // Assuming printer_results contains the text you want to print
//             // const printer_results = JSON.stringify(printer_resultsValue);
         
//             ThermalPrinter.printFormattedText({
//                 type: 'usb',
//                 id: printer.id,
//                 text: 
//                 "[C]<img>" + printer_resultsValue + "</img>\n"
//             }, function () {
//                 console.log('Successfully printed:', printer_resultsValue);
//             }, function (error) {
//                 console.error('Printing error', JSON.stringify(error));
//             });

//         } else {
//             console.error('No printers found!');
//         }
//     }, function (error) {
//         console.error('Ups, we can\'t list the printers!', error);
//     });
// }
USBPrinter() {
  console.log("555555555555555555", "working");
  const self = this;
  ThermalPrinter.listPrinters({ type: 'usb' }, function (printers) {
      if (printers.length > 0) {

        const deviceIdAsString = printers[0]?.deviceId;
        const vendorIdIdAsString = printers[0]?.vendorId;
        const productNameAsString = printers[0]?.productName;
        const manufacturerNameAsString = printers[0]?.manufacturerName;
        
        // Convert to numbers with default value of 0 if undefined
        const productNameAsNumber = parseInt(String(productNameAsString ?? '0'), 10);
        const manufacturerNameAsNumber = parseInt(String(manufacturerNameAsString ?? '0'), 10);
        const deviceIdAsNumber = parseInt(String(deviceIdAsString ?? '0'), 10);
        const vendorIdAsNumber = parseInt(String(vendorIdIdAsString ?? '0'), 10);
        
        
        // Now you have deviceIdAsNumber and vendorIdAsNumber as numbers.
        console.log("Device ID as number:", deviceIdAsNumber);
        console.log("Vendor ID as number:", vendorIdAsNumber);

        self.device_status = "Device ID: " + deviceIdAsNumber;

          var printer = {
              productName: productNameAsNumber,
              manufacturerName: manufacturerNameAsNumber,
              id: deviceIdAsNumber,
              vendorId: vendorIdAsNumber,
          };

          let encoder = new EscPosEncoder();

          let printer_resultsValue = encoder
              .initialize()
              .text('The quick brown fox jumps over the lazy dog')
              .newline()
              .encode();

          // Convert the Uint8Array to a string
          let decoder = new TextDecoder('utf-8');
          let printer_resultsString = decoder.decode(printer_resultsValue);

          ThermalPrinter.printFormattedText({
              type: 'usb',
              id: printer.id,
              text: printer_resultsString, // Send the string
          }, function () {
              console.log('Successfully printed:', printer_resultsString);
          }, function (error) {
              console.error('Printing error', JSON.stringify(error));
          });

      } else {
          console.error('No printers found!');
      }
  }, function (error) {
      console.error('Ups, we can\'t list the printers!', error);
  });
}









  async getStatusBarHeight(){
    try{
      const kranthi = await MyStatusBar.getHeight();
    }catch(error){
      
    }
  }
  async getStatusPermission(){
    try{
      const kranthi = await MyStatusBar.getPermission();
     
      console.log(JSON.stringify(kranthi));
    }catch(error){
      
    }
  }
  



}
