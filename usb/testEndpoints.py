import usb.core
import usb.util

# find our device
dev = usb.core.find(idVendor=0x0483, idProduct=0x5740)

reattach = False
if dev.is_kernel_driver_active(0):
    reattach = True
    dev.detach_kernel_driver(0)


# was it found?
if dev is None:
    raise ValueError('Device not found')

# set the active configuration. With no arguments, the first
# configuration will be the active one
dev.set_configuration()

# get an endpoint instance
cfg = dev.get_active_configuration()
intf = cfg[(1,0)]

ep = usb.util.find_descriptor(
    intf,
    # match the first OUT endpoint
    custom_match = \
    lambda e: \
        usb.util.endpoint_direction(e.bEndpointAddress) == \
        usb.util.ENDPOINT_OUT)

ep2 = usb.util.find_descriptor(
    intf,
    # match the first OUT endpoint
    custom_match = \
    lambda e: \
        usb.util.endpoint_direction(e.bEndpointAddress) == \
        usb.util.ENDPOINT_IN)


assert ep is not None
assert ep2 is not None
#print ep #0x3 OUT do zapisu! 0
#print ep2 #0x81 IN do odczytu! 1

# write the data
ep.write('v')

data = dev.read(ep2.bEndpointAddress, ep2.wMaxPacketSize)


usb.util.release_interface(dev, 0)
usb.util.release_interface(dev, 1)
usb.util.dispose_resources(dev)
# reattach the device to the OS kernel
if reattach:
    dev.attach_kernel_driver(0)

print data
