
my $SCRIPTSIZE= 128;
my $CMD_g_PARAMETER_ZAHL= 125; #number of values transmitted with a 'g' get data command
my $CMD_d_PARAMETER_ZAHL= 32; #number of values transmitted with a 'd' get data command
my $CMD_s_PARAMETER_ZAHL= 5; #number of values transmitted with a 's' get data command

my @OptionL094List= (
{s
  name => 'Firmware Version',
  type => 'OPTTYPE_STR+OPTTYPE_READONLY', len => 0, ppos => 0, min => 0, max => 0, steps => 0,
  size => 16,
  column=> 1,
  expert=> 0,
},{
  name => 'Board',
  type => 'OPTTYPE_STR+OPTTYPE_READONLY', len => 16, ppos => 0, min => 0, max => 0, steps => 0,
  size => 16,
},{
  name => 'Name',
  type => 'OPTTYPE_STR+OPTTYPE_READONLY', len => 16, ppos => 0, min => 0, max => 0, steps => 0,
  size => 16,

##--- PID tab --------------------
},{
  name => 'Gyro LPF',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 1, steps => 1,
  size => 1,
  adr => 100,
  choices => [ 'off', '1.5 ms', '3.0 ms', '4.5 ms', '6.0 ms', '7.5 ms', '9 ms' ],
  pos=>[1,1],
  expert=> 1,
},{
  name => 'Imu2 FeedForward LPF',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 1, steps => 1,
  size => 2,
  adr => 99,
  choices => [ 'off', '1.5 ms', '4 ms', '10 ms', '22 ms', '46 ms', '94 ms' ],

},{
  name => 'Low Voltage Limit',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 7, default => 1, steps => 1,
  size => 1,
  adr => 18,
  choices => [ 'off', '2.9 V/cell', '3.0 V/cell', '3.1 V/cell', '3.2 V/cell', '3.3 V/cell', '3.4 V/cell', '3.5 V/cell' ],
  pos=>[1,4],
},{
  name => 'Voltage Correction',
  type => 'OPTTYPE_UI', len => 7, ppos => 0, min => 0, max => 200, default => 0, steps => 1,
  size => 2,
  adr => 19,
  unit => '%',

},{
  name => 'Pitch P',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => 0, max => 3000, default => 400, steps => 10,
  size => 2,
  adr => 0,
  pos=> [2,1],
},{
  name => 'Pitch I',
  type => 'OPTTYPE_UI', len => 7, ppos => 1, min => 0, max => 20000, default => 1000, steps => 50,
  size => 2,
  adr => 1,
},{
  name => 'Pitch D',
  type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
  size => 2,
  adr => 2,
},{
  name => 'Pitch Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 3,

},{
  name => 'Roll P',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => 0, max => 3000, default => 400, steps => 10,
  size => 2,
  adr => 6,
  pos=> [3,1],
},{
  name => 'Roll I',
  type => 'OPTTYPE_UI', len => 7, ppos => 1, min => 0, max => 20000, default => 1000, steps => 50,
  size => 2,
  adr => 7,
},{
  name => 'Roll D',
  type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
  size => 2,
  adr => 8,
},{
  name => 'Roll Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 9,
},{
  name => 'Yaw P',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => 0, max => 3000, default => 400, steps => 10,
  size => 2,
  adr => 12,
  pos=> [4,1],
},{
  name => 'Yaw I',
  type => 'OPTTYPE_UI', len => 7, ppos => 1, min => 0, max => 20000, default => 1000, steps => 50,
  size => 2,
  adr => 13,
},{
  name => 'Yaw D',
  type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
  size => 2,
  adr => 14,
},{
  name => 'Yaw Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 15,

##--- PAN tab --------------------
},{
  name => 'Pan Mode Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 65,
  choices => \@FunctionInputChoicesList,
  column=> 1,
  expert=> 2,
},{
  name => 'Pan Mode Default Setting',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 5, default => 0, steps => 1,
  size => 1,
  adr => 66,
  choices => [ 'hold hold pan', 'hold hold hold', 'pan pan pan', 'pan hold hold', 'pan hold pan', 'hold pan pan', 'off'],
},{
  name => 'Pan Mode Setting #1',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 1, steps => 1,
  size => 1,
  adr => 67,
  choices => [ 'hold hold pan', 'hold hold hold', 'pan pan pan', 'pan hold hold', 'pan hold pan', 'hold pan pan', 'off'],
},{
  name => 'Pan Mode Setting #2',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 4, steps => 1,
  size => 1,
  adr => 68,
  choices => [ 'hold hold pan', 'hold hold hold', 'pan pan pan', 'pan hold hold', 'pan hold pan', 'hold pan pan', 'off'],
},{
  name => 'Pan Mode Setting #3',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 2, steps => 1,
  size => 1,
  adr => 69,
  choices => [ 'hold hold pan', 'hold hold hold', 'pan pan pan', 'pan hold hold', 'pan hold pan', 'hold pan pan', 'off'],

},{
  name => 'Pitch Pan (0 = hold)',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 50, default => 20, steps => 1,
  size => 2,
  adr => 4,
  column=> 2,
},{
  name => 'Pitch Pan Deadband',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 600, default => 0, steps => 10,
  size => 2,
  adr => 5,
  unit=> '°',
  pos=> [2,3],
},{
  name => 'Pitch Pan Expo',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 100, default => 0, steps => 1,
  size => 2,
  adr => 102,
  unit=> '%',

},{
  name => 'Roll Pan (0 = hold)',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 50, default => 20, steps => 1,
  size => 2,
  adr => 10,
  column=> 3,
},{
  name => 'Roll Pan Deadband',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 600, default => 0, steps => 10,
  size => 2,
  adr => 11,
  unit=> '°',
  pos=> [3,3],
},{
  name => 'Roll Pan Expo',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 100, default => 0, steps => 1,
  size => 2,
  adr => 103,
  unit=> '%',

},{
  name => 'Yaw Pan (0 = hold)',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 50, default => 20, steps => 1,
  size => 2,
  adr => 16,
  column=> 4,
},{
  name => 'Yaw Pan Deadband',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 100, default => 50, steps => 5,
  size => 2,
  adr => 17,
  unit=> '°',
  pos=> [4,3],
},{
  name => 'Yaw Pan Expo',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 100, default => 0, steps => 1,
  size => 2,
  adr => 104,
  unit=> '%',
},{
  name => 'Yaw Pan Deadband LPF',
  type => 'OPTTYPE_UI', len => 5, ppos => 2, min => 0, max => 200, default => 0, steps => 5,
  size => 2,
  adr => 118,
  unit=> 's',

},{
  name => 'Yaw Pan Deadband Hysteresis',
  type => 'OPTTYPE_UI', len => 5, ppos => 1, min => 0, max => 50, default => 0, steps => 1,
  size => 2,
  adr => 97,
  unit=> '°',
  pos=> [4,6],

##--- RC INPUTS tab --------------------
},{
  name => 'Rc Dead Band',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 50, default => 10, steps => 1,
  size => 2,
  adr => 43,
  unit => 'us',
  expert=> 3,
},{
  name => 'Rc Hysteresis',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 50, default => 5, steps => 1,
  size => 2,
  adr => 105,
  unit => 'us',

},{
  name => 'Rc Pitch Trim',
  type => 'OPTTYPE_SI', len => 0, ppos => 0, min => -100, max => 100, default => 0, steps => 1,
  size => 2,
  adr => 46,
  unit => 'us',
  pos=>[1,4],
},{
  name => 'Rc Roll Trim',
  type => 'OPTTYPE_SI', len => 0, ppos => 0, min => -100, max => 100, default => 0, steps => 1,
  size => 2,
  adr => 53,
  unit => 'us',
},{
  name => 'Rc Yaw Trim',
  type => 'OPTTYPE_SI', len => 0, ppos => 0, min => -100, max => 100, default => 0, steps => 1,
  size => 2,
  adr => 60,
  unit => 'us',

},{
  name => 'Rc Pitch',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 44,
  choices => \@FunctionInputChoicesList,
  column => 2,
},{
  name => 'Rc Pitch Mode',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 45,
  choices => [ 'absolute', 'relative', 'absolute centered'],
},{
  name => 'Rc Pitch Min',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => -250, steps => 5,
  size => 2,
  adr => 47,
  unit => '°',
},{
  name => 'Rc Pitch Max',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 250, steps => 5,
  size => 2,
  adr => 48,
  unit => '°',
},{
  name => 'Rc Pitch Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 49,
  unit => '°/s',
},{
  name => 'Rc Pitch Accel Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 50,

},{
  name => 'Rc Roll',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 51,
  choices => \@FunctionInputChoicesList,
  column => 3,
},{
  name => 'Rc Roll Mode',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 52,
  choices => [ 'absolute', 'relative', 'absolute centered'],
},{
  name => 'Rc Roll Min',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -450, max => 450, default => -250, steps => 5,
  size => 2,
  adr => 54,
  unit => '°',
},{
  name => 'Rc Roll Max',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -450, max => 450, default => 250, steps => 5,
  size => 2,
  adr => 55,
  unit => '°',
},{
  name => 'Rc Roll Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 56,
  unit => '°/s',
},{
  name => 'Rc Roll Accel Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 57,

},{
  name => 'Rc Yaw',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 58,
  choices => \@FunctionInputChoicesList,
  column => 4,
},{
  name => 'Rc Yaw Mode',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 0, steps => 1,
  size => 1,
  adr => 59,
  choices => [ 'absolute', 'relative', 'absolute centered', 'relative turn around' ], #'relative slip ring' ],
},{
  name => 'Rc Yaw Min',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -2700, max => 2700, default => -250, steps => 10,
  size => 2,
  adr => 61,
  unit => '°',
},{
  name => 'Rc Yaw Max',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -2700, max => 2700, default => 250, steps => 10,
  size => 2,
  adr => 62,
  unit => '°',
},{
  name => 'Rc Yaw Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 63,
  unit => '°/s',
},{
  name => 'Rc Yaw Accel Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 64,

##--- FUNCTIONS tab --------------------
},{
  name => 'Standby',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 70,
  choices => \@FunctionInputChoicesList,
  expert=> 4,
  column=> 1,

},{
  name => 'Re-center Camera',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 76,
  choices => \@FunctionInputChoicesList,
  pos=>[1,3],

},{
  name => 'IR Camera Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 71,
  choices => \@FunctionInputChoicesList,
  column=> 2, #3,
},{
  name => 'Camera Model',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 0, steps => 1,
  size => 1,
  adr => 72,
  choices => [ 'Sony Nex', 'Canon', 'Panasonic', 'Nikon' ],
},{
  name => 'IR Camera Setting #1',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 73,
  choices => [ 'shutter', 'shutter delay', 'video on/off' ],
},{
  name => 'IR Camera Setting #2',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 2, steps => 1,
  size => 1,
  adr => 74,
  choices => [ 'shutter', 'shutter delay', 'video on/off', 'off' ],
},{
  name => 'Time Interval (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 150, default => 0, steps => 1,
  size => 2,
  adr => 75,
  unit => 's',

},{
  name => 'Pwm Out Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 113,
  choices => \@FunctionInputChoicesList,
  column=> 3,
},{
  name => 'Pwm Out Mid',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 900, max => 2100, default => 1500, steps => 1,
  size => 2,
  adr => 114,
  unit => 'us',
},{
  name => 'Pwm Out Min',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 900, max => 2100, default => 1100, steps => 10,
  size => 2,
  adr => 115,
  unit => 'us',
},{
  name => 'Pwm Out Max',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 900, max => 2100, default => 1900, steps => 10,
  size => 2,
  adr => 116,
  unit => 'us',
},{
  name => 'Pwm Out Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 1000, default => 0, steps => 5,
  size => 2,
  adr => 117,
  unit => 'us/s',

##--- SCRIPTS tab --------------------
},{
  name => 'Script1 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-5, #119,
  choices => \@FunctionInputChoicesList,
  expert=> 8,
  column=> 1,
},{
  name => 'Script2 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-4, #120,
  choices => \@FunctionInputChoicesList,
  column=> 2,
},{
  name => 'Script3 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-3, #121,
  choices => \@FunctionInputChoicesList,
  column=> 3,
},{
  name => 'Script4 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-2, #122,
  choices => \@FunctionInputChoicesList,
  column=> 4,

},{
  name => 'Scripts',
  type => 'OPTTYPE_SCRIPT', len => 0, ppos => 0, min => 0, max => 0, default => '', steps => 0,
  size => $SCRIPTSIZE,
  adr => $CMD_g_PARAMETER_ZAHL-1, #123,
  hidden => 1,

##--- GIMBAL SETUP tab --------------------
},{
  name => 'Imu2 Configuration',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 4, default => 0, steps => 1,
  size => 1,
  adr => 94,
  choices => [ 'off', 'full', 'full xy', 'full v1', 'full v1 xy' ],
  expert=> 5,

},{
  name => 'Acc Compensation Method',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 1, default => 1, steps => 1,
  size => 1,
  adr => 88,
  choices => [ 'standard', 'advanced'],
  pos=> [1,4],
},{
  name => 'Imu AHRS',
  type => 'OPTTYPE_UI', len => 5, ppos => 2, min => 0, max => 2500, default => 1000, steps => 100,
  size => 2,
  adr => 81,
  unit => 's',

},{
  name => 'Virtual Channel Configuration',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 10, default => 0, steps => 1,
  size => 1,
  adr => 41,
  choices => [ 'off',  'sum ppm 6', 'sum ppm 7', 'sum ppm 8', 'sum ppm 10', 'sum ppm 12',
               'spektrum 10 bit', 'spektrum 11 bit', 'sbus', 'hott sumd', 'srxl' ],
  column=> 2,

},{
  name => 'Pwm Out Configuration',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 42,
  choices => [ 'off', '1520 us 55 Hz', '1520 us 250 Hz' ],

},{
  name => 'Rc Pitch Offset',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 106,
  unit => '°',
  pos=> [2,4],
},{
  name => 'Rc Roll Offset',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 107,
  unit => '°',
},{
  name => 'Rc Yaw Offset',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 108,
  unit => '°',

},{
  name => 'Beep with Motors',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 98,
  choices => [ 'off', 'basic', 'all' ],
  pos=> [3,4],

},{
  name => 'Pitch Motor Usage',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 3, steps => 1,
  size => 1,
  adr => 78,
  choices => [ 'normal', 'level', 'startup pos', 'disabled'],
  column=> 4,
},{
  name => 'Roll Motor Usage',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 3, steps => 1,
  size => 1,
  adr => 79,
  choices => [ 'normal', 'level', 'startup pos', 'disabled'],
},{
  name => 'Yaw Motor Usage',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 3, steps => 1,
  size => 1,
  adr => 80,
  choices => [ 'normal', 'level', 'startup pos', 'disabled'],

##--- CONFIGURE GIMBAL tab  --------------------
},{
  name => 'Imu Orientation',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 23, default => 0, steps => 1,
  size => 1,
  adr => 39,
  choices => \@ImuChoicesList,
  expert=> 7,
  pos=>[1,1],
},{
  name => 'Imu2 Orientation',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 23, default => 0, steps => 1,
  size => 1,
  adr => 95,
  choices => \@ImuChoicesList,

},{
  name => 'Pitch Motor Poles',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 12, max => 28, default => 14, steps => 2,
  size => 2,
  adr => 20,
  pos=> [2,1],
},{
  name => 'Pitch Motor Direction',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 2, steps => 1,
  size => 1,
  adr => 21,
  choices => [ 'normal',  'reversed', 'auto' ],
},{
  name => 'Pitch Startup Motor Pos',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 1008, default => 504, steps => 1,
  size => 2,
  adr => 23,
},{
  name => 'Pitch Offset',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => -300, max => 300, default => 0, steps => 5,
  size => 2,
  adr => 22,
  unit=> '°',

},{
  name => 'Roll Motor Poles',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 12, max => 28, default => 14, steps => 2,
  size => 2,
  adr => 26,
  pos=> [3,1],
},{
  name => 'Roll Motor Direction',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 2, steps => 1,
  size => 1,
  adr => 27,
  choices => [ 'normal',  'reversed', 'auto' ],
},{
  name => 'Roll Startup Motor Pos',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 1008, default => 504, steps => 1,
  size => 2,
  adr => 29,
},{
  name => 'Roll Offset',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => -300, max => 300, default => 0, steps => 5,
  size => 2,
  adr => 28,
  unit=> '°',

},{
  name => 'Yaw Motor Poles',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 12, max => 28, default => 14, steps => 2,
  size => 2,
  adr => 32,
  pos=> [4,1],
},{
  name => 'Yaw Motor Direction',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 2, steps => 1,
  size => 1,
  adr => 33,
  choices => [ 'normal',  'reversed', 'auto', ],
},{
  name => 'Yaw Startup Motor Pos',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 1008, default => 504, steps => 1,
  size => 2,
  adr => 35,
},{
  name => 'Yaw Offset',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => -300, max => 300, default => 0, steps => 5,
  size => 2,
  adr => 34,
  unit=> '°',

##--- EXPERT tab  --------------------
},{
  name => 'Acc LPF',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 2, steps => 1,
  size => 1,
  adr => 85,
  choices => [ 'off', '1.5 ms', '4.5 ms', '12 ms', '25 ms', '50 ms', '100 ms' ],
  expert=> 6,
},{
  name => 'Imu DLPF',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 0, steps => 1,
  size => 1,
  adr => 86,
  choices => [ '256 Hz', '188 Hz', '98 Hz', '42 Hz', '20 Hz', '10 Hz', '5 Hz'],
},{
  name => 'Rc Adc LPF',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 0, steps => 1,
  size => 1,
  adr => 96,
  choices => [ 'off', '1.5 ms', '4.5 ms', '12 ms', '25 ms', '50 ms', '100 ms' ],
},{
  name => 'Hold To Pan Transition Time',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 1000, default => 250, steps => 25,
  size => 2,
  adr => 87,
  unit => 'ms',

},{
  name => 'Imu Acc Threshold (0 = off)',
  type => 'OPTTYPE_UI', len => 5, ppos => 2, min => 0, max => 100, default => 25, steps => 1,
  size => 2,
  adr => 84,
  unit => 'g',
  column=> 2,
},{
  name => 'Acc Noise Level',
  type => 'OPTTYPE_UI', len => 0, ppos => 3, min => 0, max => 150, default => 40, steps => 1,
  size => 2,
  adr => 89,
  unit => 'g',
},{
  name => 'Acc Threshold (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 2, min => 0, max => 100, default => 50, steps => 1,
  size => 2,
  adr => 90,
  unit => 'g',
},{
  name => 'Acc Vertical Weight',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 100, default => 25, steps => 5,
  size => 2,
  adr => 91,
  unit => '%',
},{
  name => 'Acc Zentrifugal Correction',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 100, default => 30, steps => 5,
  size => 2,
  adr => 92,
  unit => '%',
},{
  name => 'Acc Recover Time',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 1000, default => 250, steps => 5,
  size => 2,
  adr => 93,
  unit => ' ms',

},{
  name => 'Motor Mapping',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 5, default => 0, steps => 1,
  size => 1,
  adr => 40,
  choices => [ 'M0=pitch , M1=roll',  'M0=roll , M1=pitch', 'roll yaw pitch', 'yaw roll pitch', 'pitch yaw roll', 'yaw pitch roll', ],
  column=> 3,
},{
  name => 'Imu Mapping',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 1, default => 0, steps => 1,
  size => 1,
  adr => 109,
  choices => [ '1=IC2 , 2=IC2#2',  '1=IC2#2 , 2=IC2', ],
},{
  name => 'ADC Calibration',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 1000, max => 2000, default => 1550, steps => 10,
  size => 2,
  adr => 101,

},{
  name => 'NT Logging',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 7, default => 0, steps => 1,
  size => 1,
  adr => 77,
  choices => [ 'off', 'basic', 'basic + pid', 'basic + accgyro', 'basic + accgyro_raw',
               'basic + pid + accgyro', 'basic + pid + ag_raw', 'full' ],
  column=> 4,
},{
  name => 'Imu3 Configuration',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 38,
  choices => [ 'none', 'none, Imu2=on-board', 'Imu3 = NT Imu2', 'Imu3 = on-board Imu2' ],
},{
  name => 'Imu3 Orientation',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 23, default => 0, steps => 1,
  size => 1,
  adr => 83,
  choices => \@ImuChoicesList,

},{
  name => 'Mavlink Configuration',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 112,
  choices => [ 'no heartbeat', 'emit heartbeat', , 'heartbeat + attitude' ],
  pos=> [3,5],
},{
  name => 'Mavlink System ID',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 255, default => 71, steps => 1,
  size => 2,
  adr => 110,
},{
  name => 'Mavlink Component ID',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 255, default => 67, steps => 1,
  size => 2,
  adr => 111,

}
