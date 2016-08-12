import usb.core
import usb.util

# find our device
dev = usb.core.find(idVendor=0x0483, idProduct=0x5740)

# was it found?
if dev is None:
    raise ValueError('Device not found')

# set the active configuration. With no arguments, the first
# configuration will be the active one
dev.set_configuration()


reattach = False
if dev.is_kernel_driver_active(0):
    reattach = True
    dev.detach_kernel_driver(0)

endpoint_in = dev[0][(0,0)][0]
endpoint_out = dev[0][(0,0)][1]
#print ("endpoint_out",endpoint_out)
#print ("endpoint_in",endpoint_in)
