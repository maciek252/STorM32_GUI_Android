import usb.core
import usb.util
import sys
from time import gmtime, strftime
import time

print ("Meteo kezdes",strftime("%Y-%m-%d %H:%M:%S", gmtime()))


# find our device
dev = usb.core.find(idVendor=0x0483, idProduct=0x5740)

# was it found?
if dev is None:
       raise ValueError('Device not found')
else:
        print ("meteo megvan!")

reattach = False
if dev.is_kernel_driver_active(0):
        reattach = True
        dev.detach_kernel_driver(0)

endpoint_in = dev[0][(1,0)][0]
endpoint_out = dev[0][(1,0)][1]
print ("endpoint_out",endpoint_out)
print ("endpoint_in",endpoint_in)

    # write the data
msg = 'v'

if 1:
        try:
            #endpoint_out.write(msg)
            #buf = struct.pack("b",2)
            #buf1 = struct.pack("b",0)    
            #print(buf+buf1+buf1)
            dev.write(0x81,msg,0)

            # reading
            #print ("Waiting to read...")
            #print (endpoint.bEndpointAddress)
            #data = dev.read(endpoint_in.bEndpointAddress, 100, 0)
            #print data
        #except USBError, e:
            #print ("USB error")
        except Exception, e:
            print Exception, e
    # end while

# This is needed to release interface, otherwise attach_kernel_driver fails
# due to "Resource busy"
usb.util.dispose_resources(dev)

# It may raise USBError if there's e.g. no kernel driver loaded at all
if reattach:
    dev.attach_kernel_driver(0)
