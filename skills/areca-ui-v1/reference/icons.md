# Icons — `dev.voir.areca:icons`

Add it only when the app itself draws `ArecaIcons.*`. No Areca module depends on it — each module
bundles the few glyphs it draws — so without this dependency pass the app's own `ImageVector` or
`Painter` wherever a component takes an icon.

- Artifact `dev.voir.areca:icons`; package `dev.voir.areca.icons`.
- 1,062 vector icons: 1,059 converted from Lucide and Tabler Icons, plus the sign-in marks
  `SocialGoogle`, `SocialApple`, `SocialFacebook`.
- Not theme-bound: depends only on `org.jetbrains.compose.ui:ui` (`api`), not on `foundation`, and
  needs no `ArecaThemeProvider`.

## Use

Every icon is an extension property on `ArecaIcons`. Import the receiver **and** each icon:

```kotlin
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Trash
import dev.voir.areca.icons.SocialGoogle

Icon(ArecaIcons.Trash, contentDescription = deleteLabel)                     // tinted by LocalContentColor, sized by LocalIconSize
Icon(ArecaIcons.SocialGoogle, contentDescription = null, tint = Color.Unspecified)  // keeps brand colours
IconButton(ArecaIcons.Trash, deleteLabel, onClick = onDelete)
Button(text = shareLabel, onClick = onShare, leading = { Icon(ArecaIcons.Share, contentDescription = null) })
```

## Rules

- Draw through foundation `Icon` (or a component's icon parameter), never `Image`. Size with
  `IconSize.ExtraSmall` (12) … `ExtraLarge` (32) or `LocalIconSize`; every icon is 24 dp on a 24-unit
  grid with a 2-unit stroke that scales with it.
- Icons are black and any tint replaces the colour. `SocialGoogle` and `SocialFacebook` are
  full-colour: pass `tint = Color.Unspecified`. `SocialApple` is black, tinted black or white.
- Pass a localised `contentDescription` when the icon carries meaning; `null` when adjacent text says
  the same.
- **Never guess a name.** Pick from the list below; many common words differ (`House` not `Home`,
  `MarkerPin` not `MapPin`, `Menu01` not `Menu`, `FilterLines` not `Filter`, `RefreshCcw` not
  `Refresh`, `LinkExternal` not `ExternalLink`, `Microphone` not `Mic`, `MoonStar` not `Moon`).
- Aliases exist for older code and draw the same as their primary; prefer the primary: `X` (aliases
  `XClose`, `Cross`), `House` (`Home`), `CircleHelp` (`HelpCircle`), `DotsHorizontal`
  (`MoreHorizontal`), `Search` (`SearchMd`), `CrossCircle` (`XCircle`), `Eye` / `EyeOff` (`EyeShow` /
  `EyeHide`), `Zap` (`Flash`, `Lightning`), `Inbox` (`Tray`), `Sparkles` (`Stars`), `Megaphone`
  (`Announcement`), `Landmark` (`Bank`, `OnlineBanking`), `Vault` (`Safe`, `SafetyBox`), `Languages`
  (`Translate`), `Archive` (`Box`), `Gauge` (`Speedometer`), `FlipBackward` (`ReverseLeft`).
- Directional icons flip in right-to-left layouts on their own (`autoMirror`): horizontal and
  diagonal `Arrow*Left` / `Arrow*Right` (including `Circle`, `Square`, `Narrow`, `CircleBroken`,
  `Block` variants), `ArrowsLeft` / `ArrowsRight`, `ChevronLeft` / `ChevronRight` and their `Double`
  variants, every `Corner*`, `FlipBackward` / `FlipForward`, `ReverseLeft`, `LeftIndent` /
  `RightIndent`, `LogIn` / `LogOut`, `Send`, `Share`. Media transport (`Play`, `FastForward`,
  `SkipBack`), clocks, refresh and rotate arrows, text alignment and two-way arrows do not.
- Ship the licence notices (Lucide ISC, Tabler MIT) on the app's open-source licences screen; the
  artifact carries them as `META-INF/dev.voir.areca/icons/NOTICE.md` (iOS apps add it themselves).
  Use the `Social*` marks only as each provider's sign-in branding guidelines allow.
- Unreferenced icons are dropped by R8 / ProGuard and Kotlin/Native; a JVM desktop build without
  shrinking ships all of them.

## Common intents

| Intent | Icon |
| --- | --- |
| Back, forward, disclosure | `ArrowLeft`, `ArrowRight`, `ChevronLeft`, `ChevronRight`, `ChevronDown`, `ChevronUp` |
| Close, add, remove, confirm | `X`, `Plus`, `Minus`, `Check`, `CheckCircle`, `CrossCircle`, `PlusCircle` |
| Edit, delete, copy | `Edit`, `Pencil`, `Trash`, `Copy` |
| Share, send, download, upload, open elsewhere, link, attach | `Share`, `Send`, `Download`, `Upload`, `LinkExternal`, `Link`, `Paperclip` |
| Search, filter, settings, menus | `Search`, `FilterLines`, `Settings`, `Sliders`, `Menu01`, `DotsHorizontal`, `DotsVertical` |
| Home, people, notifications, messages | `House`, `User`, `Users`, `Bell`, `Mail`, `Phone` |
| Time | `Calendar`, `Clock` |
| Media and files | `Camera`, `Image`, `Video`, `Microphone`, `FileText`, `FileCheck`, `Folder`, `Archive`, `Inbox` |
| Security | `Lock`, `Unlock`, `Key`, `Fingerprint`, `Shield`, `ShieldTick`, `Eye`, `EyeOff`, `LogIn`, `LogOut` |
| Status and help | `Info`, `AlertCircle`, `AlertTriangle`, `CircleHelp` |
| Refresh, favourites | `RefreshCcw`, `Heart`, `Star`, `Bookmark` |
| Places and money | `Globe`, `MarkerPin`, `CreditCard`, `Wallet`, `ShoppingCart`, `Tag`, `Gift` |
| Devices (a sessions list) | `Smartphone`, `Tablet`, `Laptop`, `Monitor` |
| Appearance, playback, codes | `Sun`, `MoonStar`, `Play`, `PauseCircle`, `QrCode`, `Scan`, `Zap`, `Sparkles` |
| Sign-in providers | `SocialGoogle`, `SocialApple`, `SocialFacebook` |

## Every icon (aliases left out)

- **A** `Activity`, `ActivityHeart`, `AirVent`, `Airplay`, `Airpods`, `AlarmClock`, `AlarmClockCheck`, `AlarmClockMinus`, `AlarmClockOff`, `AlarmClockPlus`, `AlertCircle`, `AlertHexagon`, `AlertOctagon`, `AlertSquare`, `AlertTriangle`, `AlignCenter`, `AlignJustify`, `AlignLeft`, `AlignRight`, `Ambulance`, `Amphora`, `Anchor`, `Angle`, `Annotation`, `AnnotationAlert`, `AnnotationCheck`, `AnnotationDots`, `AnnotationHeart`, `AnnotationPlus`, `AnnotationQuestion`, `AnnotationX`, `Antenna`, `Aperture`, `AppWindow`, `Apple`, `Archive`, `Armchair`, `Armchair2`, `ArrowBlockDown`, `ArrowBlockLeft`, `ArrowBlockRight`, `ArrowBlockUp`, `ArrowCircleBrokenDownLeft`, `ArrowCircleBrokenDownRight`, `ArrowCircleBrokenUpLeft`, `ArrowCircleBrokenUpRight`, `ArrowCircleDown`, `ArrowCircleDownLeft`, `ArrowCircleDownRight`, `ArrowCircleLeft`, `ArrowCircleRight`, `ArrowCircleUp`, `ArrowCircleUpLeft`, `ArrowCircleUpRight`, `ArrowDown`, `ArrowDownLeft`, `ArrowDownRight`, `ArrowDownUp`, `ArrowLeft`, `ArrowLeftRight`, `ArrowNarrowDown`, `ArrowNarrowDownLeft`, `ArrowNarrowDownRight`, `ArrowNarrowLeft`, `ArrowNarrowRight`, `ArrowNarrowUp`, `ArrowNarrowUpLeft`, `ArrowNarrowUpRight`, `ArrowRight`, `ArrowSquareDown`, `ArrowSquareDownLeft`, `ArrowSquareDownRight`, `ArrowSquareLeft`, `ArrowSquareRight`, `ArrowSquareUp`, `ArrowSquareUpLeft`, `ArrowSquareUpRight`, `ArrowUp`, `ArrowUpLeft`, `ArrowUpRight`, `ArrowsDown`, `ArrowsLeft`, `ArrowsRight`, `ArrowsUp`, `Asterisk`, `AtSign`, `Atom`, `Attachment02`, `Award`
- **B** `Baby`, `BabyBottle`, `BabyCarriage`, `Backpack`, `BadgePercent`, `BaggageClaim`, `BallAmericanFootball`, `BallBaseball`, `BallBasketball`, `BallBowling`, `BallFootball`, `BallTennis`, `Balloon`, `Banana`, `Bandage`, `BankNote`, `BarChart`, `BarChartAsc`, `BarChartDesc`, `BarChartLine`, `BarChartLineAlt`, `BarChartLineAsc`, `BarChartLineDesc`, `BarLineChart`, `Basket`, `Bath`, `BatteryCharging`, `BatteryEmpty`, `BatteryFull`, `BatteryLow`, `BatteryMid`, `BeachUmbrella`, `Beaker`, `Bean`, `BedDouble`, `BedSingle`, `Beef`, `Beer`, `Bell`, `BellMinus`, `BellOff`, `BellPlus`, `BellRinging`, `BezierCurve`, `BicepsFlexed`, `Bike`, `Binoculars`, `Bird`, `Blocks`, `BluetoothConnect`, `BluetoothOff`, `BluetoothOn`, `BluetoothSignal`, `Bold`, `BoldAlt`, `Bone`, `BookClosed`, `BookOpen`, `BookOpenText`, `Bookmark`, `BookmarkAdd`, `BookmarkCheck`, `BookmarkMinus`, `BookmarkX`, `Bot`, `Bottle`, `BowlChopsticks`, `Brackets`, `Brain`, `BrandAndroid`, `BrandApple`, `Bread`, `BrickWall`, `Briefcase`, `BriefcaseBusiness`, `Broccoli`, `Broom`, `Brush`, `BrushAlt`, `Bug`, `Building`, `Building2`, `BuildingArch`, `BuildingMonument`, `BuildingPavilion`, `BuildingStore`, `BuildingWarehouse`, `Buildings`, `Bulb`, `Burger`, `Bus`, `BusFront`
- **C** `Cable`, `CableCar`, `Cake`, `CakeSlice`, `Calculator`, `Calendar`, `CalendarCheck`, `CalendarCheckAlt`, `CalendarDate`, `CalendarDollar`, `CalendarHeart`, `CalendarHeartAlt`, `CalendarMinus`, `CalendarMinusAlt`, `CalendarPlus`, `CalendarPlusAlt`, `CalendarX`, `Camera`, `CameraOff`, `CameraPlus`, `Camper`, `CanSoda`, `Candle`, `CandlestickChart`, `Candy`, `Cannabis`, `Car`, `CarCrash`, `CarFan`, `CarFront`, `CarGarage`, `CarSuv`, `CarTaxiFront`, `Caravan`, `CardSim`, `Carrot`, `Cash`, `CashBanknote`, `CashBanknoteMove`, `CashRegister`, `Cast`, `Castle`, `Cat`, `Cctv`, `Certificate`, `ChartUp`, `Check`, `CheckCircle`, `CheckCircleBroken`, `CheckDone`, `CheckHeart`, `CheckSquare`, `CheckSquareBroken`, `CheckVerified`, `Cheese`, `ChefHat`, `Cheque`, `Cherry`, `Chess`, `ChevronDown`, `ChevronDownDouble`, `ChevronLeft`, `ChevronLeftDouble`, `ChevronRight`, `ChevronRightDouble`, `ChevronSelectorHorizontal`, `ChevronSelectorVertical`, `ChevronUp`, `ChevronUpDouble`, `ChristmasTree`, `Church`, `Cigarette`, `Circle`, `CircleHelp`, `CircleParking`, `Citrus`, `Clapperboard`, `Clipboard`, `ClipboardCheck`, `ClipboardMinus`, `ClipboardPlus`, `ClipboardX`, `Clock`, `ClockCheck`, `ClockFastForward`, `ClockPlus`, `ClockRewind`, `ClockSnooze`, `ClockStopwatch`, `Cloud`, `CloudAlt`, `CloudLightning`, `CloudMoon`, `CloudOff`, `CloudRain`, `CloudSnowing`, `CloudSun`, `Clover`, `Code`, `CodeAlt`, `Codepen`, `Coffee`, `Coin`, `Coins`, `CoinsStacked`, `CoinsSwap`, `Colors`, `Command`, `Compass`, `Computer`, `ConciergeBell`, `Confetti`, `Container`, `Contrast`, `Cookie`, `CookingPot`, `Copy`, `CornerDownLeft`, `CornerDownRight`, `CornerLeftDown`, `CornerLeftUp`, `CornerRightDown`, `CornerRightUp`, `CornerUpLeft`, `CornerUpRight`, `CpuChip`, `CreditCard`, `CreditCardCheck`, `CreditCardMinus`, `CreditCardPlus`, `CreditCardReader`, `CreditCardRefresh`, `CreditCardX`, `Croissant`, `Crop`, `CrossCircle`, `Crown`, `Crutches`, `Cryptocurrency`, `Cube`, `Cup`, `CupSoda`, `Cupcake`, `CurrencyBitcoin`, `CurrencyDollar`, `CurrencyEthereum`, `CurrencyEuro`, `CurrencyEuroCircle`, `CurrencyPound`, `CurrencyPoundCircle`, `CurrencyRuble`, `CurrencyRupee`, `CurrencyRupeeCircle`, `CurrencyYen`, `CurrencyYenCircle`, `Cursor`, `CursorAlt`, `CursorBox`, `CursorClick`, `Cut`
- **D** `Database`, `DatabaseAlt`, `Dataflow`, `Delete`, `Dental`, `DeviceLaptop`, `DeviceMobileDollar`, `DeviceTvOld`, `DeviceWatch`, `Dice1`, `Dice2`, `Dice3`, `Dice4`, `Dice5`, `Dice6`, `Dices`, `Disc`, `Discount`, `Divide`, `DivideCircle`, `Dna`, `Dog`, `Donut`, `DoorOpen`, `Dotpoints`, `DotsGrid`, `DotsHorizontal`, `DotsVertical`, `Download`, `DownloadAlt`, `DownloadCloud`, `DownloadCloudAlt`, `Drama`, `Drill`, `Drone`, `Droplet`, `DropletHalf2`, `Droplets`, `Dropper`, `Drum`, `Drumstick`, `Dumbbell`
- **E** `Ear`, `Earth`, `EarthPin`, `Edit`, `EditLine`, `EditSquare`, `Egg`, `EggFried`, `Engine`, `Equal`, `EqualNot`, `Eraser`, `EvCharger`, `Exchange`, `Expand`, `ExpandSquare`, `Eye`, `EyeOff`
- **F** `FaceContent`, `FaceFrown`, `FaceHappy`, `FaceId`, `FaceIdSquare`, `FaceNeutral`, `FaceSad`, `FaceSmile`, `FaceWink`, `Factory`, `Fan`, `FastBackward`, `FastForward`, `Faucet`, `Feather`, `Fence`, `FerrisWheel`, `Ferry`, `Figma`, `FileChart`, `FileCheck`, `FileCode`, `FileCodeAlt`, `FileDownload`, `FileEmpty`, `FileHeart`, `FileLink`, `FileLock`, `FileMinus`, `FilePlus`, `FileQuestion`, `FileSearch`, `FileShieldText`, `FileText`, `FileX`, `Film`, `FilterFunnel`, `FilterLines`, `Fingerprint`, `Fish`, `FishBone`, `FishSymbol`, `FishingRod`, `Flag01`, `FlagWave`, `Flame`, `FlameKindling`, `FlipBackward`, `FlipForward`, `Flower`, `Flower2`, `Folder`, `FolderCheck`, `FolderClosed`, `FolderCode`, `FolderDownload`, `FolderLock`, `FolderMinus`, `FolderPlus`, `FolderQuestion`, `FolderSearch`, `FolderX`, `Footprints`, `Forbidden`, `Frame`, `Framer`, `Friends`, `Fuel`
- **G** `Gamepad2`, `GasStation`, `Gauge`, `Gavel`, `Gem`, `Gift`, `GiftCard`, `GitBranch`, `GitCommit`, `GitMerge`, `GitPullRequest`, `GlassChampagne`, `GlassCocktail`, `GlassWater`, `Glasses`, `Globe`, `Goal`, `Golf`, `GoogleChrome`, `GraduationHat`, `Grape`, `Guitar`
- **H** `Ham`, `Hammer`, `Hand`, `HandCoins`, `HandFist`, `HandHeart`, `HandHelping`, `HandLuggage`, `HandPlatter`, `Handbag`, `Handshake`, `Hanger`, `HardDrive`, `HardHat`, `Hash`, `HashAlt`, `HatGlasses`, `Heading`, `HeadingAlt`, `Headphones`, `Headset`, `Heart`, `HeartCircle`, `HeartFill`, `HeartHandshake`, `HeartPlus`, `HeartRounded`, `HeartRoundedFill`, `Heartbeat`, `Hearts`, `Heater`, `Helicopter`, `Helmet`, `HelpBubble`, `HelpHexagon`, `HelpOctagon`, `HelpSquare`, `Hexagon`, `Home01`, `HomeAlt`, `HomeDollar`, `HomeShield`, `HomeSmile`, `HorizontalBarChart`, `HorizontalBarChartAsc`, `HorizontalBarChartDesc`, `Horse`, `HorseToy`, `Horseshoe`, `Hospital`, `Hotel`, `Hourglass`, `House`, `Hurricane`
- **I** `IceCreamCone`, `IdBadge2`, `IdCard`, `IdCardLanyard`, `Image`, `ImageCheck`, `ImageCircle`, `ImageDown`, `ImagePlus`, `ImageUp`, `ImageUser`, `ImageX`, `Inbox`, `Infinity`, `Info`, `InfoCircle`, `InfoHexagon`, `InfoOctagon`, `InfoSquare`, `Invoice`, `Ironing`, `Italic`, `ItalicAlt`
- **J** `Joystick`
- **K** `Kayak`, `Key`, `KeyRound`, `Keyboard`
- **L** `Lamp`, `LampCeiling`, `Landmark`, `Languages`, `Laptop`, `LaptopMinimal`, `LawnMower`, `Leaf`, `Leaf2`, `LeafyGreen`, `LeftIndent`, `LetterSpacing`, `Library`, `LibraryBig`, `License`, `LifeBuoy`, `Lightbulb`, `LightningAlt`, `LineChartDown`, `LineChartUpArrow`, `LineHeight`, `Link`, `LinkAlt`, `LinkBroken`, `LinkExternal`, `LinkExternalSquare`, `Loading`, `Lock`, `LockKeyholeSquare`, `LockUnlocked`, `LogIn`, `LogOut`, `Luggage`
- **M** `MagicWand01`, `MagicWand02`, `Mail`, `MailAlt`, `MailAltOpen`, `MailOpen`, `Mailbox`, `Map`, `MapAlt`, `Mark`, `MarkerPin`, `Martini`, `Massage`, `Medal`, `MedicalCross`, `Megaphone`, `Menu01`, `MessageAlertCircle`, `MessageAlertSquare`, `MessageChatCircle`, `MessageChatSquare`, `MessageCheckCircle`, `MessageCheckSquare`, `MessageCircle`, `MessageCircleAlt`, `MessageDotsCircle`, `MessageDotsSquare`, `MessageHeart`, `MessageHeartCircle`, `MessageHeartSquare`, `MessageNotificationSquare`, `MessagePlusCircle`, `MessagePlusSquare`, `MessageQuestionSquare`, `MessageSquare`, `MessageSquareAlt`, `MessageTextSquare`, `MessageXCircle`, `MessageXSquare`, `Microphone`, `Microphone2`, `MicrophoneOff`, `Microscope`, `Microwave`, `Milk`, `Minus`, `MinusCircle`, `MinusSquare`, `MirrorRound`, `MoneyBag`, `Monitor`, `MoodKid`, `MoonAlt`, `MoonStar`, `Mop`, `Moped`, `Mosque`, `Motorbike`, `Mountain`, `MountainSnow`, `Mouse`, `Move`, `Movie`, `MusicNote`, `MusicNotePlus`
- **N** `NavigationPointer`, `NavigationPointerForward`, `NavigationPointerForwardOff`, `NavigationPointerOff`, `NeedleThread`, `News`, `Newspaper`, `Nfc`, `Notebook`
- **O** `Octagon`, `Old`, `Origami`
- **P** `Package`, `PackageCheck`, `PackageMinus`, `PackageOpen`, `PackagePlus`, `PackageSearch`, `PackageX`, `Paint`, `PaintPour`, `PaintRoller`, `Paintbrush`, `Palette`, `PaperBag`, `Paperclip`, `ParagraphWrap`, `PartyPopper`, `Passcode`, `Passport`, `PauseCircle`, `PauseSquare`, `PawPrint`, `PcCase`, `PenTool`, `Pencil`, `PencilLine`, `PencilRuler`, `Pentagon`, `Percent`, `PercentCircle`, `Percentage`, `Perfume`, `Perspective`, `Phone`, `PhoneCall`, `PhoneCallAlt`, `PhoneHangUp`, `PhoneIncoming`, `PhoneOutgoing`, `PhonePause`, `PhonePlus`, `PhoneX`, `Piano`, `PieChart`, `PigMoney`, `PiggyBank`, `Pilcrow`, `PilcrowSquare`, `Pill`, `PillBottle`, `Pin`, `PinAlt`, `Pizza`, `Plane`, `PlaneLanding`, `PlaneTakeoff`, `PlantPot`, `Play`, `PlayCircle`, `PlayFootball`, `PlaySquare`, `Plug`, `Plus`, `PlusCircle`, `PlusSquare`, `Podcast`, `Podium`, `Pool`, `Popcorn`, `Power`, `Pray`, `Presentation`, `PresentationChart`, `Printer`, `Puzzle`
- **Q** `QrCode`
- **R** `Rabbit`, `Radio`, `Razor`, `Receipt`, `ReceiptCheck`, `ReceiptTax`, `Recording`, `Recycle`, `Reflect`, `RefreshCcw`, `Refrigerator`, `Repeat`, `RepeatOval`, `ReportMoney`, `Ribbon`, `RightIndent`, `Rings`, `Road`, `RoadSign`, `Rocket`, `RocketLaunch`, `RockingChair`, `Rose`, `RotateCcwSquare`, `Route`, `Router`, `Rss`, `RubberStamp`, `Ruler`, `Run`
- **S** `Sailboat`, `Salad`, `Salt`, `Sandwich`, `SatelliteDish`, `Sausage`, `Save`, `Scale`, `Scales`, `Scan`, `School`, `Scissors`, `ScissorsCut`, `Scooter`, `ScooterElectric`, `ScrollText`, `Search`, `SearchX`, `Seedling`, `Send`, `Server`, `Settings`, `SettingsControl`, `Share`, `ShareNodes`, `ShelvingUnit`, `Shield`, `ShieldDollar`, `ShieldOff`, `ShieldPlus`, `ShieldTick`, `ShieldZap`, `Ship`, `Shirt`, `ShirtSport`, `Shoe`, `ShoppingBag03`, `ShoppingBasket`, `ShoppingCart`, `ShoppingCartDollar`, `Shovel`, `ShowerHead`, `Shrimp`, `Shuffle`, `Sigma`, `Signature`, `Siren`, `Skateboarding`, `SkiJumping`, `SkipBack`, `SkipForward`, `SlashCircle`, `SlashDivider`, `Sliders`, `Smartphone`, `Smoking`, `Snowboarding`, `Snowflake`, `SoapDispenserDroplet`, `SocialApple`, `SocialFacebook`, `SocialGoogle`, `Sofa`, `SolarPanel`, `Soup`, `Spade`, `Sparkle`, `Sparkles`, `Speaker`, `SportShoe`, `SprayCan`, `Sprout`, `Square`, `Stamp`, `Star`, `StarAlt`, `SteeringWheel`, `Stethoscope`, `StickerSquare`, `StopCircle`, `StopSquare`, `Store`, `Strikethrough`, `StrikethroughAlt`, `Subscript`, `Sun`, `Sunglasses`, `Sunrise`, `Sunset`, `Swimming`, `SwissFranc`, `SwitchHorizontal`, `SwitchHorizontalAlt`, `SwitchVertical`, `SwitchVerticalAlt`, `Syringe`
- **T** `Tablet`, `Tag`, `TagAlt`, `Target`, `TargetAlt`, `Telescope`, `Tent`, `Terminal`, `TerminalBrowser`, `TerminalSquare`, `TestPipe`, `TestTube`, `TextInput`, `Thermometer`, `ThermometerCold`, `ThermometerLow`, `ThermometerWarm`, `ThumbsDown`, `ThumbsUp`, `Ticket`, `TicketCheck`, `Tickets`, `ToggleLeft`, `ToggleRight`, `Toilet`, `Tool`, `Toolbox`, `Tools`, `ToolsKitchen2`, `Toothbrush`, `TowerControl`, `ToyBrick`, `Tractor`, `TrafficCone`, `Train`, `TrainFrontTunnel`, `TramFront`, `Transform`, `Trash`, `TrashAlt`, `TreeDeciduous`, `TreePalm`, `Trees`, `Triangle`, `Trophy`, `Truck`, `TruckReturn`, `TubeLotion`, `TurkishLira`, `Turtle`, `Tv`, `TvMinimalPlay`, `Type`, `TypeAlt`
- **U** `Umbrella`, `Underline`, `UnderlineAlt`, `University`, `Unlock`, `Upload`, `UploadAlt`, `UploadCloud`, `UploadCloudAlt`, `UsbFlashDrive`, `User`, `UserCheck`, `UserCircle`, `UserDown`, `UserEdit`, `UserMinus`, `UserPlus`, `UserUp`, `UserX`, `Users`, `UsersGroup`, `UsersMinus`, `UsersPlus`, `UsersRound`, `Utensils`, `UtensilsCrossed`, `UtilityPole`
- **V** `Vaccine`, `VacuumCleaner`, `Van`, `Variable`, `Vault`, `Vegan`, `Video`, `VideoRecorderOff`, `Virus`, `Voicemail`, `Volleyball`, `VolumeMax`, `VolumeMin`, `VolumeX`
- **W** `Walk`, `Wallet`, `WalletAlt`, `WalletMinimal`, `Warehouse`, `WarningSeal`, `Wash`, `WashingMachine`, `Watch`, `Waves`, `Webcam`, `Wheat`, `Wheel`, `Wheelchair`, `Whistle`, `Wifi`, `WifiOff`, `Wind`, `WindAlt`, `Wine`, `World`, `Wrench`
- **X** `X`, `XSquare`
- **Y** `Yarn`, `Yoga`, `Youtube`
- **Z** `Zap`, `ZapOff`, `ZoomIn`, `ZoomOut`
