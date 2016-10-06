/*********************************************************************************************
Author: Leo Wang
Start Date: 2015.03.13
Version: 3.0
Copyright(c): Glisee co.,ltd.
Description: Provides core Html5 functions & event callbacks to display our 3D product.
Notice: Ah~ The day when I started writing this javascript is my sister 's birthday, 
	maybe she inspired me to finish the 1st version so quickly, best wishes for her 
	family & career! I spent a lot of time thinking about it, eventually I made up my
	mind to start this brand new js. Though I haven 't learnt js, I can give it a shot.
*********************************************************************************************/

//Macros-------------------------------------------------------------------------------------,
//Defines whether this javascript is running in development or product mode.
//Just like a marco, each mode has its own settings, and different codes
//will be triggered as well, e.g. Log print function only works in development mode.
//var runningMode = "Development";
var runningMode = "Product";

//Defines the log print mode.
var logMode = "OneLine";	//Only show the latest line.
//var logMode = "Cascade";	//New lines will follow the old ones.
//Macros-------------------------------------------------------------------------------------'

//Global variables---------------------------------------------------------------------------,
var log;		//Log element.
var canvasBG;	//Background canvas element.
var canvasUI;	//UI canvas element.
var contextBG;	//Context of background canvas.
var contextUI;	//Context of UI canvas.

var controlPool;	//A control group that manages all the children controls.
var loadingProgBar;	//Progress bar shows the loading percentage.
var autoSpinBtn;	//Button switches auto-spin on or off.
var othkinLogoBtn;
var touchDownCtl;

var loadDT;	//Date time of when the page starts to load.

var uiAnimationHwnd;//Handle points to an async callback(UI animation).

var fingerDown;		//Whether the touch screen is being touched.
var canSlide;
var isBgColorSet;

var MainPhotoLayers;
var bufImages;		//For ultra high res image.
var isUltraResNeededList;
var maxLayerIdx;	//Index of the layer with highest resolution.
var maxSplitCount;	
var MainPhotos;
var MainPhotosTiny;	//Array that stores all the main photos.
var colCount;		//Photo amount per round.
var rowCount;		//Camera count.
var startIdxX;		//First shown main photo index in x axis.
var startIdxY;		//First shown main photo index in y axis.
var currentIdxX;	//Current horizontal main photo index.
var currentIdxY;	//Current vertical main photo index.
var lastIdxX;		//Last horizontal main photo index.
var lastIdxY;		//Last vertical main photo index.
var isUltraResNeeded;
var photoWidth;		//Original photo width(In pixels, in photo coordinates system).
var photoHeight;	//Original photo height(In pixels, in photo coordinates system).
var offsetX;		//X offset of the rendered photo in screen coordinates system.
var offsetY;		//Y offset of the rendered photo in screen coordinates system.
var renderWidth;	//Rendering width on screen(In pixels).
var renderHeight;	//Rendering height on screen(In pixels).
var lastRW;
var lastRH;
var startWidth;		//Buffers the initial renderWidth of each zooming action.
var startHeight;	//Buffers the initial renderHeight of each zooming action.
var zoomCenterX;	//Zooming center x(In pixels, in coordinates system of the photo with startWidth).
var zoomCenterY;	//Zooming center y(In pixels, in coordinates system of the photo with startHeight).
var aspectRatioPhoto;	//Aspect ratio of the photo(Fixed).
var aspectRatioCanvas;	//Aspect ratio of the screen(Changes when the mobile screen is rotating).
var autoSpinSpeed;	//-10 ~ 10, negative means anti-clockwise, 0 means no auto-spin.
var autoSpinSpdCoe;	//A coefficient multiplied by autoSpinSpeed.
var autoSpinDist;	//Integration of auto-spin velocity(Will shift the x index of frame when the value goes over the limit).
var lastMillisec;
var startX0;	//X of the first finger when starting dragging.
var startY0;	//Y of the first finger when starting dragging.
var startX1;	//X of the second finger when starting dragging.
var startY1;	//Y of the second finger when starting dragging.
var dragSpeedX;	
var dragSpeedY;
var dragDistX;	//Integration of dragSpeedX.
var dragDistY;
var inertiaX;
var inertiaY;
var inertiaDistX;	//Integration of inertiaX.
var inertiaDistY;
var startSpdCoeX;
var speedCoeX;	//A coefficient to calculate the dragSpeed in axis x.
var startDist;	//Start distance of multiple finger positions.
var scaleCoe;	//Zooming coefficient.
var lastScaleCoe;	//Stores the zooming coefficient of last gesture.
var maxScaleCoe;	//Maximum zooming coefficient.
var scaleOrg;
var centerX;	//Center X of multiple finger positions.
var centerY;	//Center Y of multiple finger positions.
var bgRed;
var bgGreen;
var bgBlue;
var touchUpX;	//Used for checking UI control hitting as I can not get finger pos in TouchUp() event.
var touchUpY;
var clickCount;	//Stores single touch count only.
var firstTouchX;
var firstTouchY;
var autoZoom;	//-1 = Zoom-out, 1 = Zoom-in, 0 = Do nothing.
//Global variables---------------------------------------------------------------------------'

//Global functions---------------------------------------------------------------------------,
function Log(msg)
{
	if ("Development" == runningMode)
	{
		if ("OneLine" == logMode)
			log.innerHTML = msg;
		else if ("Cascade" == logMode)
			log.innerHTML += "<br/>" + msg;
	}
}

function Initialize()
{
	loadDT = new Date();
	if ("Development" == runningMode)
	{
		log = document.getElementById("Log");
	}
	canvasBG = document.getElementById("CanvasBG");
	contextBG = canvasBG.getContext("2d");
	canvasUI = document.getElementById("CanvasUI");
	contextUI = canvasUI.getContext("2d");
	loadingProgBar = null;
	autoSpinBtn = null;	
	othkinLogoBtn = null;
	uiAnimationHwnd = null;
	fingerDown = false;
	canSlide = true;
	MainPhotoLayers = [];
	MainPhotos = null;
	MainPhotosTiny = null;
	dragSpeedX = 0;
	dragSpeedY = 0;
	dragDistX = 0;
	dragDistY = 0;
	inertiaX = dragSpeedX;
	inertiaY = dragSpeedY;
	inertiaDistX = 0;
	inertiaDistY = 0;
	startSpdCoeX = 3;	//An empiric value(> 0), it can be changed to improve user experiences.
	scaleCoe = 1.0;
	lastScaleCoe = scaleCoe;
	bgRed = bgR;
	bgGreen = bgG;
	bgBlue = bgB;
	isBgColorSet = false;
	touchUpX = -1;
	touchUpY = -1;
	touchDownCtl = null;
	clickCount = 0;
	autoZoom = 0;
	controlPool = ControlPool.BuildNew();
	//Log("Initialization complete.");
}

var _srcDirUrl;
function LoadPhotos(srcDirUrl, p_RowCount, p_ColCount, p_PhotoWidth, p_PhotoHeight)
{
	_srcDirUrl = srcDirUrl;
	colCount = p_ColCount;
	rowCount = p_RowCount;
	startIdxX = 0;	//Temporarily set to const 0, later it will be sent in as a parameter.
	startIdxY = 0;	//Temporarily set to const 0, later it will be sent in as a parameter.
	currentIdxX = startIdxX;
	currentIdxY = startIdxY;
	lastIdxX = -1;
	lastIdxY = -1;
	isUltraResNeeded = true;
	photoWidth = p_PhotoWidth;
	photoHeight = p_PhotoHeight;
	centerX = 0;
	centerY = 0;
	autoSpinSpeed = 0;	//Temporarily set to 0, later it will be sent in as a parameter.
	autoSpinSpdCoe = 1.5;	//Empiric value.
	autoSpinDist = 0;
	lastMillisec = 0;
	//To get adaptive switch speed, we need to calculate how many degrees it has rotated
	//despite of colCount. Be aware that we can only perform this calculation on axis x
	//as we don 't have a 360 degrees circle in vertical direction.
	var anglePerFrameX = 360 / colCount;
	startSpdCoeX *= anglePerFrameX;
	FullScreen();
	DrawLoadingProgressBar();
	DrawAutoSpinBtn();
	// if (!customizedLogo) DrawOthkinLogoBtn();
	// else DrawCustomizedLogoBtn(customizedLogo);
		
	
	
	
	
	
	var completeCount = 0;
	if (3.0 <= h5Ver)
	{
		bufImages = [];
		isUltraResNeededList = [];
		var startSplitLv = 8;
		maxLayerIdx = -1;
		maxSplitCount = 0;
		for (var i = 0; i < 8; i++)
		{
			if (lODFlag >> (7 - i) & 1)
			{
				MainPhotoLayers[i] = [];
				for (var j = 0; j < rowCount; j++)
				{
					for (var k = 0; k < colCount; k++)
					{
						MainPhotoLayers[i][j * colCount + k] = [];
						var splitCount;
						if (i < startSplitLv) splitCount = 1;
						else splitCount = 2 << (i - startSplitLv);
						maxLayerIdx = i;
						maxSplitCount = splitCount;	//The last loop will get the max value.
						var count = 0;
						for (var y = 0; y < splitCount; y++)
						{
							for (var x = 0; x < splitCount; x++, count++)
							{
								// MainPhotoLayers[i][j * colCount + k][count] = i.toString() + "/" + j.toString() + "_" + k.toString() + "/" + y.toString() + "_" + x.toString() + ".jpg";
								MainPhotoLayers[i][j * colCount + k][count] = "00"+ (k>9?k.toString():("0"+k.toString())) + ".jpg";
							}
						}
					}
				}
			}
		}
		//Init buffer images.
		for (var i = 0; i < maxSplitCount * maxSplitCount; i++)
		{
			bufImages[i] = new Image();	//Init src = "".
			isUltraResNeededList[i] = false;
		}
		MainPhotosTiny = [];
		for (var j = 0; j < rowCount; j++) 
		{
			MainPhotosTiny[j] = [];
			for (var i = 0; i < colCount; i++) 
			{
				MainPhotosTiny[j][i] = new Image();
				MainPhotosTiny[j][i].src = srcDirUrl + MainPhotoLayers[lv0][j * colCount + i][0];
				if (MainPhotosTiny[j][i].complete)
				{
					completeCount++;
					loadingProgBar.SetValue(completeCount / (colCount * rowCount) * 100);
					GetBgColor();
					//Auto spin the product when 10 photos are completely downloaded,
					//10 is an empiric value.
					if (10 == completeCount)
					{
						ProceedWithInitBehavior();
					}
				}
				else
				{
					MainPhotosTiny[j][i].onload = function()
					{
						completeCount++;
						GetBgColor();
						loadingProgBar.SetValue(completeCount / (colCount * rowCount) * 100);
						//Auto spin the product when 10 photos are completely downloaded,
						//10 is an empiric value.
						if (10 == completeCount)
						{
							ProceedWithInitBehavior();
						}
					}
				}
			}
		}
		
		if (-1 != lvSD) //If lvSD == -1, that means this product has only 1 layer, therefore MainPhotos should be valued null
		//so Render() will not cause crush.
		{
			MainPhotos = [];
			for (var j = 0; j < rowCount; j++) 
			{	
				MainPhotos[j] = [];
				//The 1st photo should be downloaded first, and the rest shall be downloaded in reverse sequence
				//so that it will not switch between lv0 && lvSD frequently during the init 360 rotating.
				MainPhotos[j][0] = new Image();
				MainPhotos[j][0].src = srcDirUrl + MainPhotoLayers[lvSD][j * colCount][0];
				for (var i = colCount - 1; i > 0; i--) 
				{
					MainPhotos[j][i] = new Image();
					MainPhotos[j][i].src = srcDirUrl + MainPhotoLayers[lvSD][j * colCount + i][0];
				}
			} 
		}
	}
	else
	{
		lvSD = 1;
		MainPhotoLayers[lvSD]=[];
		for(var e=0;e<rowCount;e++)
		{
			for (var g=0; g<colCount; g++)
			{
				MainPhotoLayers[lvSD][e * colCount + g] = [];
				MainPhotoLayers[lvSD][e * colCount + g][0]=e.toString() + "_" + g.toString() + ".jpg";
			}
		}
		
		MainPhotos = [];
		for (var j = 0; j < rowCount; j++) 
		{	
			MainPhotos[j] = [];
			for (var i = 0; i < colCount; i++) 
			{
				MainPhotos[j][i] = new Image();
				MainPhotos[j][i].src = srcDirUrl + MainPhotoLayers[lvSD][j * colCount + i][0];
				if (MainPhotos[j][i].complete)
				{
					completeCount++;
					GetBgColor();
					loadingProgBar.SetValue(completeCount / (colCount * rowCount) * 100);	
				}
				else MainPhotos[j][i].onload = function()
				{
					completeCount++;
					GetBgColor();
					loadingProgBar.SetValue(completeCount / (colCount * rowCount) * 100);		
				}
			}
		} 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	window.onorientationchange = OrientationChange;		
	window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || 
	window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;
	requestAnimationFrame(RenderLoop); 	
}

function GetTimeInterval(startDT)
{
	if ("Development" == runningMode)
	{
		var nowDT = new Date();
		var loadingTimeTotalMS = nowDT.getTime() - startDT.getTime();
		var loadingTimeMin = Math.floor(loadingTimeTotalMS / 60000);
		var loadingTimeSec = Math.floor((loadingTimeTotalMS - loadingTimeMin * 60000) / 1000);
		var loadingTimeMS = loadingTimeTotalMS % 1000;
		var loadingTimeStr = "Downloading time spent:";
		if (0 < loadingTimeMin) loadingTimeStr += " " + loadingTimeMin + " min(s)";
		if (0 < loadingTimeSec) loadingTimeStr += " " + loadingTimeSec + " sec(s)";
		loadingTimeStr += " " + loadingTimeMS + " millisecond(s).";
		Log(loadingTimeStr);
	}
}

function FullScreen()
{
	var scrWidth = document.documentElement.clientWidth;	//Visible area width of the browser.
	var scrHeight = document.documentElement.clientHeight;	//Visible area height of the browser.
	scrHeight -= cvsHeightDec;
	//var scrWidth = window.screen.availWidth;	//Visible area width of the browser.
	//var scrHeight = window.screen.availHeight;	//Visible area height of the browser.
	Log(scrWidth + ", " + scrHeight);
	canvasBG.width = scrWidth;
	canvasBG.height = scrHeight;
	canvasUI.width = scrWidth;
	canvasUI.height = scrHeight;
	aspectRatioCanvas = canvasBG.width / canvasBG.height;
	aspectRatioPhoto = photoWidth / photoHeight;
	if (aspectRatioCanvas > aspectRatioPhoto)
	{
		renderHeight = canvasBG.height;
		renderWidth = (renderHeight * aspectRatioPhoto + 0.5) | 0;
		offsetX = (((canvasBG.width - renderWidth) >> 1) + 0.5) | 0;
		offsetY = 0;
		scaleOrg = photoHeight / canvasBG.height;
	}
	else if (aspectRatioCanvas < aspectRatioPhoto)
	{
		renderWidth = canvasBG.width;
		renderHeight = (renderWidth / aspectRatioPhoto + 0.5) | 0;
		offsetX = 0;
		offsetY = (((canvasBG.height - renderHeight) >> 1) + 0.5) | 0;
		scaleOrg = photoWidth / canvasBG.width;
	}
	else
	{
		renderWidth = canvasBG.width;
		renderHeight = canvasBG.height;
		offsetX = 0;
		offsetY = 0;
		scaleOrg = photoWidth / canvasBG.width;
	}
	startWidth = renderWidth;
	startHeight = renderHeight;
	lastRW = renderWidth;
	lastRH = renderHeight;
	lastScaleCoe = 1.0;
	maxScaleCoe = 12;	//An empiric value.
	//Note that maxScaleCoe refers to original photo resolution, so I have to multiply a coefficient.
	maxScaleCoe *= scaleOrg;
	
	dragSpeedX = 0;
	dragSpeedY = 0;
	dragDistX = 0;
	dragDistY = 0;
	inertiaX = dragSpeedX;
	inertiaY = dragSpeedY;
	inertiaDistX = 0;
	inertiaDistY = 0;
	//We also need to multiply a coefficient to speedCoeX removes the connection with screen resolution.
	//I think it 's best to rotate about 180 degrees with a drag crosses the shorter edge of the
	//screen, this value can be changed at any time for better user experiences.
	var shorterLen = canvasUI.width > canvasUI.height ? canvasUI.height : canvasUI.width;
	//I will use 768 px as a standard.
	speedCoeX = startSpdCoeX * shorterLen / 768;
	//Redraw the main photo in current screen orientation by resetting these 2 variables to -1,
	//so that the render loop will repaint it.
	lastIdxX = -1;
	lastIdxY = -1;
	
	//Update UI layer.	
	if (loadingProgBar)
	{
		loadingProgBar.MoveTo(canvasUI.width / 3, 20, canvasUI.width / 3, 60, 1);
	}
	// if (autoSpinBtn)
	// {
	// 	var shorter = canvasUI.width > canvasUI.height ? canvasUI.height : canvasUI.width;
	// 	var btnWidth = shorter / 8;
	// 	var btnHeight = btnWidth;
	// 	autoSpinBtn.MoveTo((canvasUI.width - btnWidth) >> 1, canvasUI.height - btnHeight * 1.2, btnWidth, btnHeight, 1);	
	// }
	if (othkinLogoBtn)
	{
		var aspectRatio = othkinLogoBtn.bBox.height / othkinLogoBtn.bBox.width;
		var newWidth = canvasUI.width / 3;
		var newHeight = newWidth * aspectRatio;
		othkinLogoBtn.MoveTo(10, 10, newWidth, newHeight, 1);
	}
}

function ProceedWithInitBehavior()
{
	var index = 0;
	while (null != initActions[index])
	{
		switch (initActions[index])
		{
			case "Rotate#360": initRotatePhotoAmount = colCount; break;
		}
		index++;
	}
}

//Get the background color of main photo.
function GetBgColor()
{
	// if (3.0 > h5Ver && !MainPhotos[startIdxY][startIdxX].complete) return;
	// if (3.0 <= h5Ver && !MainPhotosTiny[startIdxY][startIdxX].complete) return;
	// if (isBgColorSet) return;
	// var tempCanvas = document.createElement('canvas');  
	// tempCanvas.width = 1;  
	// tempCanvas.height = 1;  
	// var tempContext = tempCanvas.getContext("2d"); 
	// if (3.0 > h5Ver) tempContext.drawImage(MainPhotos[startIdxY][startIdxX], 10, 10, 1, 1, 0, 0, 1, 1);
	// else tempContext.drawImage(MainPhotosTiny[startIdxY][startIdxX], 10, 10, 1, 1, 0, 0, 1, 1);
	// var imgData = tempContext.getImageData(0, 0, 1, 1);
	// bgRed = imgData.data[0];
	// bgGreen = imgData.data[1];
	// bgBlue = imgData.data[2];
	// isBgColorSet = true;
	// delete tempCanvas;
	// Log("BackColor: " + bgRed + ", " + bgGreen + ", " + bgBlue);
}

//@Param vecX: Vector in axis x, positive or minus.
//@Param vecY: Vector in axis y, positive or minus.
function SwitchPhotoIdx(vecX, vecY)
{	
	var p_currentIdxX = currentIdxX;
	var p_currentIdxY = currentIdxY;
	if (0 < vecX) 
	{
		p_currentIdxX++;
	}
	else if (0 > vecX) 
	{
		p_currentIdxX--;
	}
	
	if (0 < vecY) 
	{
		p_currentIdxY++;
	}
	else if (0 > vecY) 
	{
		p_currentIdxY--;
	}
	
	//Process in x axis is a bit different, just in case of >1 delta,
	//this method will provide a smooth switch when it goes from one end to the other.
	if (0 > p_currentIdxX) p_currentIdxX += colCount;
	else if (colCount <= p_currentIdxX) p_currentIdxX = p_currentIdxX - colCount;
	
	if (0 > p_currentIdxY) { p_currentIdxY = 0; vecY = 0; }
	else if (rowCount <= p_currentIdxY) { p_currentIdxY = rowCount - 1; vecY = 0; }
	
	//Check if the photo has already been downloaded.
	if (3.0 <= h5Ver && MainPhotosTiny[p_currentIdxY][p_currentIdxX].complete)
	{
		currentIdxX = p_currentIdxX;
		currentIdxY = p_currentIdxY;
		isUltraResNeeded = true;
		return true;
	}
	else if (3.0 > h5Ver && MainPhotos[p_currentIdxY][p_currentIdxX].complete)
	{
		currentIdxX = p_currentIdxX;
		currentIdxY = p_currentIdxY;
		isUltraResNeeded = true;
		return true;
	}
	else 
	{
		Log("Photo is unavailable.");
		return false;
	}
}

//Show photo.
//@Return: 0 = Successful
//		   1 = Only tiny main photo was rendered
//		   2 = No main photo was rendered
function Render(p_currentIdxX, p_currentIdxY)
{
	//Render background layer.
	ClearCanvasGhost();
	
 	if (MainPhotos && MainPhotos[p_currentIdxY][p_currentIdxX].complete)
	{
		contextBG.drawImage(MainPhotos[p_currentIdxY][p_currentIdxX], offsetX, offsetY, renderWidth, renderHeight);
		return 0;
	} 
	else if (3.0 <= h5Ver && MainPhotosTiny[p_currentIdxY][p_currentIdxX].complete)
	{
		contextBG.drawImage(MainPhotosTiny[p_currentIdxY][p_currentIdxX], offsetX, offsetY, renderWidth, renderHeight);		
		//var cvs = document.createElement("canvas");
		//stackBlurImage(cvs, MainPhotosTiny[p_currentIdxY][p_currentIdxX], 3);
		//contextBG.drawImage(cvs, offsetX, offsetY, renderWidth, renderHeight);
		return 1;
	}
	else return 2;
}

function Render3(p_bufImage, p_offsetX, p_offsetY, p_renderWidth, p_renderHeight)
{
	contextBG.drawImage(p_bufImage, p_offsetX, p_offsetY, p_renderWidth, p_renderHeight);
}

//Note that this function only checks the min size now.
function ValidateRenderRectSize()
{
	var resFlag = true;
	if (aspectRatioCanvas <= aspectRatioPhoto)
	{
		if (renderWidth < canvasBG.width)
		{
			renderWidth = canvasBG.width;
			renderHeight = renderWidth / aspectRatioPhoto;
			lastScaleCoe = 1;
			resFlag = false;
		}
	}
	else
	{
		if (renderHeight < canvasBG.height)
		{
			renderHeight = canvasBG.height;
			renderWidth = renderHeight * aspectRatioPhoto;
			lastScaleCoe = 1;
			resFlag = false;
		}
	}
	
	return resFlag;
}

//Check if the coordinates of rendering rectangle is legal, and drag it back to legal range if not.
//<+>I can cut down some CPU usage by returning a flag to tell whether it is needed or not to render
//a new frame.
function ValidateRenderRectPos()
{
	if (aspectRatioPhoto >= aspectRatioCanvas)
	{
		if (0 < offsetX) offsetX = 0;
		else if (offsetX + renderWidth < canvasBG.width) offsetX = canvasBG.width - renderWidth;
		
		if (renderHeight < canvasBG.height)
		{
			offsetY = (canvasBG.height - renderHeight) >> 1;
		}
		else
		{
			if (0 < offsetY) offsetY = 0;
			else if (offsetY + renderHeight < canvasBG.height) offsetY = canvasBG.height - renderHeight;
		}	
	}
	else
	{
		if (0 < offsetY) offsetY = 0;
		else if (offsetY + renderHeight < canvasBG.height) offsetY = canvasBG.height - renderHeight;
		
		if (renderWidth < canvasBG.width)
		{
			offsetX = (canvasBG.width - renderWidth) >> 1;
		}
		else
		{
			if (0 < offsetX) offsetX = 0;
			else if (offsetX + renderWidth < canvasBG.width) offsetX = canvasBG.width - renderWidth;
		}
	}
}

function Zoom(newScaleCoe)
{
	lastScaleCoe *= newScaleCoe;
	if (maxScaleCoe < lastScaleCoe) lastScaleCoe = maxScaleCoe;
	else
	{
		lastRW = renderWidth;
		lastRH = renderHeight;
		renderWidth *= newScaleCoe;
		renderHeight *= newScaleCoe;
		if (!ValidateRenderRectSize()) 
		{				
			ValidateRenderRectPos();		
			//Render new frame here directly for RenderLoop() 
			//will not do it when lastScaleCoe == 1.
			Render(currentIdxX, currentIdxY);	
			return;
		}
		var deltaX = (renderWidth - lastRW) * zoomCenterX / startWidth;
		var deltaY = (renderHeight - lastRH) * zoomCenterY / startHeight;
		offsetX -= deltaX;
		offsetY -= deltaY;
		ValidateRenderRectPos();
	}
}

//Clear the canvas with background color if needed(E.g. Ghost will appear 
//on the photo boundary if the photo is smaller than the canvas).
function ClearCanvasGhost()
{
	if (renderWidth < canvasBG.width)
	{
		contextBG.fillStyle = "rgb(" + bgRed + ", " + bgGreen + ", " + bgBlue + ")";
		var blockWidth = canvasBG.width - renderWidth >> 1;
		contextBG.fillRect(0, 0, blockWidth, canvasBG.height);	
		contextBG.fillRect(renderWidth + blockWidth, 0, blockWidth, canvasBG.height);	
	}
	if (renderHeight < canvasBG.height)
	{	
		contextBG.fillStyle = "rgb(" + bgRed + ", " + bgGreen + ", " + bgBlue + ")";
		var blockHeight = canvasBG.height - renderHeight >> 1;
		contextBG.fillRect(0, 0, canvasBG.width, blockHeight);	
		contextBG.fillRect(0, renderHeight + blockHeight, canvasBG.width, blockHeight);	
	}
}
//Global functions---------------------------------------------------------------------------'

//Global callbacks---------------------------------------------------------------------------,
function RenderLoop()
{
	//Init-behavior process.
	if (0 < initRotatePhotoAmount)
	{
		//Init rotating should stop when human interaction starts.
		if (fingerDown || 0 != autoSpinSpeed) initRotatePhotoAmount = 0;
		else
		{
			initSpinDist += 5 * autoSpinSpdCoe;
			if (speedCoeX < Math.abs(initSpinDist))
			{
				if (SwitchPhotoIdx(1, 0))
				{
					initRotatePhotoAmount--;
					initSpinDist = 0;
				}
			}
		}
	}
	
	//Auto-spin process.
	if (!fingerDown && 0 != autoSpinSpeed)
	{
		/* autoSpinDist += autoSpinSpeed * autoSpinSpdCoe;
		if (speedCoeX < Math.abs(autoSpinDist))
		{
			SwitchPhotoIdx(autoSpinDist, 0);
			autoSpinDist = 0;
		} */
		
		var dateNow = new Date();
		var millisec = dateNow.getMilliseconds();
		var timeInterval = (millisec - lastMillisec + 1000) % 1000;
		if (speedCoeX * (11 - Math.abs(autoSpinSpeed)) < timeInterval) 
		{
			SwitchPhotoIdx(autoSpinSpeed, 0);
			lastMillisec = millisec;
		}
	}

	//Inertia process.
	if (!fingerDown && (0 != inertiaX || 0 != inertiaY))
	{
		var delta = 0.9;	//Empiric value that shows the weaken speed of the inertia.
		if (0 < inertiaX) { inertiaX *= delta; if (0.1 > inertiaX) inertiaX = 0; }
		else if (0 > inertiaX) { inertiaX *= delta; if (-0.1 < inertiaX) inertiaX = 0; }
		if (0 < inertiaY) { inertiaY *= delta; if (0.1 > inertiaY) inertiaY = 0; }
		else if (0 > inertiaY) { inertiaY *= delta; if (-0.1 < inertiaY) inertiaY = 0; }
		Log(inertiaX);
		//if (1 >= lastScaleCoe)
		{
			inertiaDistX += inertiaX;
			inertiaDistY += inertiaY;
			if (speedCoeX < Math.abs(inertiaDistX))
			{
				SwitchPhotoIdx(inertiaDistX, 0);
				inertiaDistX = 0;
			}
			if (speedCoeX < Math.abs(inertiaDistY))
			{
				SwitchPhotoIdx(0, inertiaDistY);
				inertiaDistY = 0;
			}
		}
		/*else
		{
			offsetX += inertiaX;
			offsetY += inertiaY;			
			ValidateRenderRectPos();
		}*/
	}
	
	//Auto zooming.
	if (1 == autoZoom)
	{
		Zoom(1.07);
		if (renderWidth > 2 * photoWidth) autoZoom = 0;
	}
	else if (-1 == autoZoom)
	{
		Zoom(0.93);
		if (1 >= lastScaleCoe) autoZoom = 0;
	}
	
	var renderRes = 0;
	if (1 >= lastScaleCoe)
	{
		if (lastIdxX != currentIdxX || lastIdxY != currentIdxY) 
		{
			renderRes = Render(currentIdxX, currentIdxY);
		}
	}
	else
	{
		if (isUltraResNeeded)
		{
			for (var i = 0; i < maxSplitCount * maxSplitCount; i++)
			{	
				isUltraResNeededList[i] = true;
			}
			isUltraResNeeded = false;
		}
		if (3.0 > h5Ver || !fingerDown && 0 != autoSpinSpeed) Render(currentIdxX, currentIdxY);
		else if (-1 != lvMax)
		{
			for (var i = 0; i < maxSplitCount * maxSplitCount; i++)
			{
				if (isUltraResNeededList[i])
				{
					bufImages[i].src = _srcDirUrl + MainPhotoLayers[lvMax][currentIdxY * colCount + currentIdxX][i];		
					isUltraResNeededList[i] = false;
				}
			}
			var completeCount = 0;
			var tileWidth = renderWidth / maxSplitCount;
			var tileHeight = renderHeight / maxSplitCount;
			for (var y = 0; y < maxSplitCount; y++)
			{
				for (var x = 0; x < maxSplitCount; x++)
				{			
					if (bufImages[y * maxSplitCount + x].complete) 
					{
						completeCount++;
					}
				}
			}			
			ClearCanvasGhost();
			if (completeCount < maxSplitCount * maxSplitCount) Render(currentIdxX, currentIdxY);
			//else
			//{
				for (var y = 0; y < maxSplitCount; y++)
				{
					for (var x = 0; x < maxSplitCount; x++)
					{			
						if (bufImages[y * maxSplitCount + x].complete) 
						{
							Render3(bufImages[y * maxSplitCount + x], offsetX + tileWidth * x, offsetY + tileHeight * y, tileWidth, tileHeight);
						}
					}
				}
			//}
		}
		else
		{
			Render(currentIdxX, currentIdxY);
		}
	}
	
	if (0 == renderRes)
	{
		lastIdxX = currentIdxX;
		lastIdxY = currentIdxY;
	}
	
	//UI.
	controlPool.HandleLoop();
	
	requestAnimationFrame(RenderLoop);
}

function OrientationChange() 
{
    switch(window.orientation) 
	{
    　　case 0: 
            //alert("肖像模式 0,screen-width: " + canvasBG.width + "; screen-height:" + canvasBG.height);
            //break;
    　　case -90: 
            //alert("左旋 -90,screen-width: " + canvasBG.width + "; screen-height:" + canvasBG.height);
            //break;
    　　case 90:   
            //alert("右旋 90,screen-width: " + canvasBG.width + "; screen-height:" + canvasBG.height);
            //break;
    　　case 180:   
			//I have to delay the canvas updating for sometimes it still
			//gets old resolution of the document client in my tests(My 
			//WP 8.1 Lumia 526 + WeChat 6.0.2 specifically).
			//<?>So the question is: Is it an async process?
			setTimeout("FullScreen()", 300);	//300ms is an empiric value.
        　　//alert("风景模式 180,screen-width: " + canvasBG.width + "; screen-height:" + canvasBG.height);
			break;
    }
}

function TouchDown(event)
{
	event = event || windows.event;
	event.preventDefault();
	
	fingerDown = true;
	
	//Show UI layer.
	clearTimeout(uiAnimationHwnd);
	// var y = canvasUI.height - autoSpinBtn.bBox.width - 140;
	// if (isNoFooter) y = canvasUI.height - autoSpinBtn.bBox.width * 1.2;
	// autoSpinBtn.MoveTo((canvasUI.width - autoSpinBtn.bBox.width) >> 1, 
	// y, 
	// autoSpinBtn.bBox.width, autoSpinBtn.bBox.height, 10);	
	
	if (1 == event.touches.length)
	{
		startX0 = event.touches[0].pageX;
		startY0 = event.touches[0].pageY;
		touchUpX = startX0;
		touchUpY = startY0;
		//First we shall check if we hit a control or something like it.
		//If it is, we should not stop the inertia.
		touchDownCtl = controlPool.CheckTarget(startX0, startY0);
		if (!touchDownCtl)
		{
			iDistX = 0;
			iDistY = 0;
			inertiaX = 0;
			inertiaY = 0;
			
			clickCount++;
			if (1 == clickCount)
			{
				firstTouchX = startX0;
				firstTouchY = startY0;
				setTimeout(function() { clickCount = 0; }, 400);
			}
			else if (2 == clickCount)
			{
				//Check if the 2 touches are in the same area.
				var dist = Math.sqrt(Math.pow(startX0 - firstTouchX, 2) + Math.pow(startY0 - firstTouchY, 2));
				if (50 > dist)
				{
					zoomCenterX = startX0 - offsetX;
					zoomCenterY = startY0 - offsetY;
					startWidth = renderWidth;
					startHeight = renderHeight;
					//Here I use 1.2 because sometimes the user may not
					//zoom out the photo to 1:1 scale, it 's an empiric
					//value.
					if (1.2 >= lastScaleCoe) autoZoom = 1;
					else autoZoom = -1;
				}
				clickCount = 0;
			}
		}
	}
	else if (2 == event.touches.length) 
	{
		clickCount = 0;
		startX0 = event.touches[0].pageX;
		startY0 = event.touches[0].pageY;
		startX1 = event.touches[1].pageX;
		startY1 = event.touches[1].pageY;
		startDist = Math.pow((Math.pow((startX1 - startX0), 2) + Math.pow((startY1 - startY0), 2)), 0.5);
		centerX = (startX0 + startX1) / 2;
		centerY = (startY0 + startY1) / 2;
		zoomCenterX = centerX - offsetX;
		zoomCenterY = centerY - offsetY;
		startWidth = renderWidth;
		startHeight = renderHeight;
	}
}

function TouchMove(event) 
{
	event = event || window.event;
	event.preventDefault();
	//Rotating viewpoint mode.
	//This will only change the current photo indices, the rendering
	//will be executed in analog render thread instead.
	if (1 == event.touches.length) 
	{
		if (canSlide)
		{
			var x = event.touches[0].pageX;
			var y = event.touches[0].pageY;
			touchUpX = x;
			touchUpY = y;
			if (1 == clickCount)
			{
				var dist = Math.sqrt(Math.pow(x - firstTouchX, 2), Math.pow(y - firstTouchY, 2));
				if (40 < dist) clickCount = 0;
			}
			var vecX = x - startX0;
			var vecY = y - startY0;
			dragSpeedX = vecX;
			dragSpeedY = vecY;
			dragDistX += dragSpeedX;
			dragDistY += dragSpeedY;
			//if (1 == lastScaleCoe)
			{
				if (speedCoeX < Math.abs(dragDistX))
				{
					//Auto-spin direction will also be influenced by manual dragging.
					if (0 < dragDistX) autoSpinSpeed = Math.abs(autoSpinSpeed);
					else autoSpinSpeed = -Math.abs(autoSpinSpeed);
					SwitchPhotoIdx(dragDistX, 0);
					dragDistX = 0;
				}
				if (speedCoeX < Math.abs(dragDistY))
				{
					SwitchPhotoIdx(0, dragDistY);
					dragDistY = 0;
				}
				Log(currentIdxY + ", " + currentIdxX + ". " + MainPhotos[currentIdxY][currentIdxX].complete);	
			}
			/*else
			{
				offsetX += vecX;
				offsetY += vecY;
				ValidateRenderRectPos();
			}*/
			startX0 = x;
			startY0 = y;
		}
	} 
	//Zooming mode.
	else if (2 == event.touches.length) 
	{
		var x0 = event.touches[0].pageX;
		var y0 = event.touches[0].pageY;
		var x1 = event.touches[1].pageX;
		var y1 = event.touches[1].pageY;
		var newCenterX = (x0 + x1) / 2;
		var newCenterY = (y0 + y1) / 2;
			var vecX = newCenterX - centerX;
			var vecY = newCenterY - centerY;
			offsetX += vecX;
			offsetY += vecY;
		var dist = Math.sqrt(Math.pow((x1 - x0), 2) + Math.pow((y1 - y0), 2));
		var newScaleCoe = dist / startDist;
		startDist = dist;
		Zoom(newScaleCoe);
		Log("Scale: " + lastScaleCoe);
		centerX = newCenterX;
		centerY = newCenterY;
	}
}

function TouchUp(event)
{
	event = event || window.event;
	Log(event.touches.length + " touch(es).");
	if (2 > event.touches.length)
	{
		scaleCoe = lastScaleCoe;
		//Avoid inertial slide after zooming to make better user experiences.
		//<?>It never goes into this part as it always get 0 touch on my Lumia 526.
		if (1 == event.touches.length)
		{
			Log("UnSlidable");
			//Update these 2 variables to avoid image jumping in zoom-in mode
			//when the last touch is still moving.
			startX0 = event.touches[0].pageX;
			startY0 = event.touches[0].pageY;
			canSlide = false;
			setTimeout(function() { canSlide = true; }, 200);	//200ms is an empiric value.
		}
		else if (0 == event.touches.length)
		{
			//UI control interaction check.
			var control = controlPool.CheckTarget(touchUpX, touchUpY);
			if (control && touchDownCtl == control)
			{
				control.Click();
			}
			
			//Inertia initialization.
			if (canSlide)
			{
				//We can let inertia works in zoom-in mode when auto-spin is on.
				if ("Pressed" == autoSpinBtn.status/* && 1 >= lastScaleCoe*/)
				{
					inertiaX = 0;
					inertiaY = 0;
				}
				else
				{
					inertiaX = dragSpeedX;
					inertiaY = dragSpeedY;
				}
				
				dragSpeedX = 0;
				dragSpeedY = 0;
			}
			else canSlide = true;
			fingerDown = false;
		}
	}
	
	//Hide UI layer after several seconds.
	uiAnimationHwnd = setTimeout(function()
	{
		// autoSpinBtn.MoveTo((canvasUI.width - autoSpinBtn.bBox.width) >> 1, canvasUI.height, 
		// autoSpinBtn.bBox.width, autoSpinBtn.bBox.height, 10);	
	}, 2000);
}

//Mouse event callbacks.
function MouseDown(event) 
{
	event = event || windows.event;
	event.preventDefault();
	
	fingerDown = true;
	//Show UI layer.
	clearTimeout(uiAnimationHwnd);
	// var y = canvasUI.height - autoSpinBtn.bBox.width - 140;
	// if (isNoFooter) y = canvasUI.height - autoSpinBtn.bBox.width * 1.2;
	// autoSpinBtn.MoveTo((canvasUI.width - autoSpinBtn.bBox.width) >> 1, 
	// y, 
	// autoSpinBtn.bBox.width, autoSpinBtn.bBox.height, 10);	
	
	startX0 = event.clientX;
	startY0 = event.clientY;
	touchUpX = startX0;
	touchUpY = startY0;
	//First we shall check if we hit a control or something like it.
	//If it is, we should not stop the inertia.
	touchDownCtl = controlPool.CheckTarget(startX0, startY0);
	if (!touchDownCtl)
	{
		iDistX = 0;
		iDistY = 0;
		inertiaX = 0;
		inertiaY = 0;
		
		clickCount++;
		if (1 == clickCount)
		{
			firstTouchX = startX0;
			firstTouchY = startY0;
			setTimeout(function() { clickCount = 0; }, 400);
		}
		else if (2 == clickCount)
		{
			//Check if the 2 touches are in the same area.
			var dist = Math.sqrt(Math.pow(startX0 - firstTouchX, 2) + Math.pow(startY0 - firstTouchY, 2));
			if (50 > dist)
			{
				zoomCenterX = startX0 - offsetX;
				zoomCenterY = startY0 - offsetY;
				startWidth = renderWidth;
				startHeight = renderHeight;
				//Here I use 1.2 because sometimes the user may not
				//zoom out the photo to 1:1 scale, it 's an empiric
				//value.
				if (1.2 >= lastScaleCoe) autoZoom = 1;
				else autoZoom = -1;
			}
			clickCount = 0;
		}
	}
}

function MouseMove(event) 
{	
	startWidth = renderWidth;
	startHeight = renderHeight;
	centerX = event.clientX;
	centerY = event.clientY;
	zoomCenterX = centerX - offsetX;
	zoomCenterY = centerY - offsetY;
	if (!fingerDown) return;
	event = event || window.event;
	event.preventDefault();
	if (canSlide)
	{
		var x = event.clientX;
		var y = event.clientY;
		touchUpX = x;
		touchUpY = y;
		if (1 == clickCount)
		{
			var dist = Math.sqrt(Math.pow(x - firstTouchX, 2), Math.pow(y - firstTouchY, 2));
			if (40 < dist) clickCount = 0;
		}
		var vecX = x - startX0;
		var vecY = y - startY0;
		dragSpeedX = vecX;
		dragSpeedY = vecY;
		dragDistX += dragSpeedX;
		dragDistY += dragSpeedY;
		//Rotating viewpoint mode.
		//This will only change the current photo indices, the rendering
		//will be executed in analog render thread instead.
		if (1 >= lastScaleCoe)
		{
			Log(speedCoeX + " : " + dragDistX);
			if (speedCoeX < Math.abs(dragDistX))
			{
				//Auto-spin direction will also be influenced by manual dragging.
				if (0 < dragDistX) autoSpinSpeed = Math.abs(autoSpinSpeed);
				else autoSpinSpeed = -Math.abs(autoSpinSpeed);
				SwitchPhotoIdx(dragDistX, 0);
				dragDistX = 0;
			}
			if (speedCoeX < Math.abs(dragDistY))
			{
				SwitchPhotoIdx(0, dragDistY);
				dragDistY = 0;
			}
			//Log(currentIdxY + ", " + currentIdxX + ". " + MainPhotosTiny[currentIdxY][currentIdxX].complete);	
		}
		//Photo translating mode.
		else
		{
			offsetX += vecX;
			offsetY += vecY;
			ValidateRenderRectPos();
		}
		startX0 = x;
		startY0 = y;
	}
}

function MouseUp(event) 
{
	event = event || window.event;
	scaleCoe = lastScaleCoe;
	//UI control interaction check.
	var control = controlPool.CheckTarget(touchUpX, touchUpY);
	if (control && touchDownCtl == control)
	{
		control.Click();
	}
	
	//Inertia initialization.
	if (canSlide)
	{
		//We can let inertia works in zoom-in mode when auto-spin is on.
		if ("Pressed" == autoSpinBtn.status && 1 >= lastScaleCoe)
		{
			inertiaX = 0;
			inertiaY = 0;
		}
		else
		{
			inertiaX = dragSpeedX;
			inertiaY = dragSpeedY;
		}
		
		dragSpeedX = 0;
		dragSpeedY = 0;
	}
	else canSlide = true;
	fingerDown = false;
	
	//Hide UI layer after several seconds.
	uiAnimationHwnd = setTimeout(function()
	{
		// autoSpinBtn.MoveTo((canvasUI.width - autoSpinBtn.bBox.width) >> 1, canvasUI.height, 
		// autoSpinBtn.bBox.width, autoSpinBtn.bBox.height, 10);	
	}, 2000);
}

//Zooming.
function MouseWheel(event)
{
	event = event || window.event;
	event.preventDefault();
	if (0.0 < event.wheelDelta)
	{
		Zoom(1.15);
	}
	else if (0.0 > event.wheelDelta)
	{
		Zoom(0.85);
	}
	Log("Scale: " + lastScaleCoe);
}
//Global callbacks---------------------------------------------------------------------------'

//UI-----------------------------------------------------------------------------------------,
function DrawLoadingProgressBar()
{	
	//<?>I use 40px font, but I can 't use 40 as height here, it seems the characters take
	//up more than 40 pixels in axis y.
	loadingProgBar = ProgressBar.BuildNew(canvasUI.width / 3, 20, canvasUI.width / 3, 60);
	controlPool.Add(loadingProgBar);
}

function DrawAutoSpinBtn()
{	
	var shorter = canvasUI.width > canvasUI.height ? canvasUI.height : canvasUI.width;
	var btnWidth = shorter / 8;
	var btnHeight = btnWidth;
	var y = canvasUI.height - btnHeight - 140;
	if (isNoFooter) y = canvasUI.height - btnHeight * 1.2;
	// autoSpinBtn = Button.BuildNew(
	// (canvasUI.width - btnWidth) >> 1, y, btnWidth, btnHeight,
	// "img/AutoSpinNormal.png", "img/AutoSpinPressed.png");
autoSpinBtn = Button.BuildNew(
	(canvasUI.width -btnWidth/2 - 30), canvasUI.height/2, btnWidth, btnHeight,
	"img/AutoSpinNormal.png", "img/AutoSpinPressed.png");
	autoSpinBtn.OnClick = function()
	{
		if (0 == autoSpinSpeed) 
		{
			if (0 <= inertiaX) autoSpinSpeed = 7;
			else autoSpinSpeed = -7;
		}
		else autoSpinSpeed = 0;
	}
	controlPool.Add(autoSpinBtn);
}

function DrawOthkinLogoBtn()
{
	var newWidth = canvasUI.width / 3;
	if (canvasUI.width > canvasUI.height) newWidth = canvasUI.height / 3;
	var newHeight = newWidth / 4.21;
	othkinLogoBtn = Button.BuildNew(10, 10, newWidth, newHeight,
	"img/Logo.png", "img/Logo.png");
	//othkinLogoBtn.Hide();
	othkinLogoBtn.OnClick = function()
	{
		// window.location.href = "";
	}
	controlPool.Add(othkinLogoBtn);
}

function DrawCustomizedLogoBtn(logoUrl)
{
	if ("None" == logoUrl) return;
	var newWidth = canvasUI.width  * 2/ 5;
	if (canvasUI.width > canvasUI.height) newWidth = canvasUI.height * 2/ 5;
	var newHeight = newWidth;
	var tempImg = new Image();
	tempImg.src = logoUrl;
	if (tempImg.complete)
	{
		if (tempImg.width >= tempImg.height)
		{
			if (tempImg.width < newWidth)
			{
				newWidth = tempImg.width;
				newHeight = tempImg.height;
			}
			else
			{
				newHeight = newWidth * tempImg.height / tempImg.width;
			}
		}
		else
		{		
			if (tempImg.height < newHeight)
			{
				newWidth = tempImg.width;
				newHeight = tempImg.height;
			}
			else
			{
				newWidth = newHeight * tempImg.width / tempImg.height;
			}
		}
		othkinLogoBtn = Button.BuildNew(10, 10, newWidth, newHeight,
		logoUrl, logoUrl);
		/*othkinLogoBtn.OnClick = function()
		{
			window.location.href = "http://m.othkin.icoc.cc";
		}*/
		controlPool.Add(othkinLogoBtn);
	}
	else
	{
		tempImg.onload = function()
		{
			if (tempImg.width >= tempImg.height)
			{
				if (tempImg.width < newWidth)
				{
					newWidth = tempImg.width;
					newHeight = tempImg.height;
				}
				else
				{
					newHeight = newWidth * tempImg.height / tempImg.width;
				}
			}
			else
			{		
				if (tempImg.height < newHeight)
				{
					newWidth = tempImg.width;
					newHeight = tempImg.height;
				}
				else
				{
					newWidth = newHeight * tempImg.width / tempImg.height;
				}
			}
			othkinLogoBtn = Button.BuildNew(10, 10, newWidth, newHeight,
			logoUrl, logoUrl);
			/*othkinLogoBtn.OnClick = function()
			{
				window.location.href = "http://m.othkin.icoc.cc";
			}*/
			controlPool.Add(othkinLogoBtn);
		}
	}
}
//Some UI "class" definition.
function BoundingBox(x, y, width, height)
{
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
} 

var Control = 
{
	BuildNew: function(x, y, width, height)
	{
		x = (x + 0.5) | 0;
		y = (y + 0.5) | 0;
		width = (width + 0.5) | 0;
		height = (height + 0.5) | 0;
		var control = {};
		control.bBox = new BoundingBox(x, y, width, height);
		control.lastBBox = new BoundingBox(x, y, width, height);	//Used for validating.
		//Used for UI animation.------,
		control.startBBox = new BoundingBox(x, y, width, height);
		control.endBBox = new BoundingBox(x, y, width, height);	
		control.frameIdxNow = 0;	
		control.frameCount = 0;
		//Used for UI animation.------'
		control.needValidate = false;
		control.visible = true;	//Whether it is visible.
		control.enable = true;	//Whether it is able to interact with.
		control.iUpdate = null;	//Update interface for inherited "class".
		
		control.Show = function() { control.visible = true; control.needValidate = true; }
		
		control.Hide = function() { control.visible = false; control.needValidate = true; }
		
		control.MoveTo = function(endX, endY, endWidth, endHeight, frameCount) 
		{ 
			control.startBBox.x = control.bBox.x;
			control.startBBox.y = control.bBox.y;
			control.startBBox.width = control.bBox.width;
			control.startBBox.height = control.bBox.height;
			control.endBBox.x = (endX + 0.5) | 0;
			control.endBBox.y = (endY + 0.5) | 0;
			control.endBBox.width = (endWidth + 0.5) | 0;
			control.endBBox.height = (endHeight + 0.5) | 0;
			control.frameCount = frameCount;
			control.frameIdxNow = 0;
			NextFrame();
			control.needValidate = true;
		}
		
		var NextFrame = function() 
		{ 
			control.frameIdxNow++;
			var coe = control.frameIdxNow / control.frameCount;
			var newX = control.startBBox.x + (control.endBBox.x - control.startBBox.x) * coe;
			var newY = control.startBBox.y + (control.endBBox.y - control.startBBox.y) * coe;
			var newWidth = control.startBBox.width + (control.endBBox.width - control.startBBox.width) * coe;
			var newHeight = control.startBBox.height + (control.endBBox.height - control.startBBox.height) * coe;
			control.bBox.x = (newX + 0.5) | 0;
			control.bBox.y = (newY + 0.5) | 0;
			control.bBox.width = (newWidth + 0.5) | 0;
			control.bBox.height = (newHeight + 0.5) | 0;
		}
		
		//This should be invoked when needValidate is true.
		control.Update = function()
		{
			contextUI.clearRect(control.lastBBox.x, control.lastBBox.y, control.lastBBox.width, control.lastBBox.height);
			control.lastBBox.x = control.bBox.x;
			control.lastBBox.y = control.bBox.y;
			control.lastBBox.width = control.bBox.width;
			control.lastBBox.height = control.bBox.height;
			if (control.visible) { control.iUpdate(); }
			else control.needValidate = false;
			if (control.frameIdxNow >= control.frameCount) control.needValidate = false;
			else NextFrame();
		}
		
		return control;
	}
};

var Button = 
{
	BuildNew: function(x, y, width, height, normalImgUrl, pressedImgUrl)
	{
		x = (x + 0.5) | 0;
		y = (y + 0.5) | 0;
		width = (width + 0.5) | 0;
		height = (height + 0.5) | 0;
		var button = Control.BuildNew(x, y, width, height);
		button.type = "Switch";	//2 types: Switch & key(Type "Key" is not done yet).
		button.status = "Normal";	//2 status: normal & pressed.
		var normalImg = null;
		var pressedImg = null;
		var cvsNormalImg = null;	//Buffer canvas(Use canvas instead of image in order to boost render speed).
		var cvsPressedImg = null;	//Buffer canvas(Use canvas instead of image in order to boost render speed).
		
		//Use default appearance if normalImgUrl || pressedImgUrl are null.
		if (!normalImgUrl || !pressedImgUrl)
		{
			contextUI.fillStyle = "#00FFFF";
			contextUI.fillRect(x, y, width, height);
		}
		else
		{
			normalImg = new Image();
			normalImg.src = normalImgUrl;
			if (normalImg.complete)
			{
				cvsNormalImg = document.createElement("canvas");
				cvsNormalImg.width = width;
				cvsNormalImg.height = height;
				var ctxNormalImg = cvsNormalImg.getContext("2d");
				ctxNormalImg.drawImage(normalImg, 0, 0, cvsNormalImg.width, cvsNormalImg.height);
				contextUI.drawImage(cvsNormalImg, x, y, width, height);
			}
			else
			{
				normalImg.onload = function()
				{
					cvsNormalImg = document.createElement("Canvas");
					cvsNormalImg.width = width;
					cvsNormalImg.height = height;
					var ctxNormalImg = cvsNormalImg.getContext("2d");
					ctxNormalImg.drawImage(normalImg, 0, 0, cvsNormalImg.width, cvsNormalImg.height);
					contextUI.drawImage(cvsNormalImg, x, y, width, height);				
				}
			}
			
			pressedImg = new Image();
			pressedImg.src = pressedImgUrl;
			if (pressedImg.complete)
			{
				cvsPressedImg = document.createElement("canvas");
				cvsPressedImg.width = width;
				cvsPressedImg.height = height;
				var ctxPressedImg = cvsPressedImg.getContext("2d");
				ctxPressedImg.drawImage(pressedImg, 0, 0, cvsPressedImg.width, cvsPressedImg.height);
			}
			else
			{
				pressedImg.onload = function()
				{
					cvsPressedImg = document.createElement("Canvas");
					cvsPressedImg.width = width;
					cvsPressedImg.height = height;
					var ctxPressedImg = cvsPressedImg.getContext("2d");
					ctxPressedImg.drawImage(pressedImg, 0, 0, cvsPressedImg.width, cvsPressedImg.height);	
				}
			}
		}
		
		button.OnPress = function()
		{		 		
			button.status = "Pressed";
			button.needValidate = true;
		}
		
		button.OnRelease = function()
		{
			button.status = "Normal";
			button.needValidate = true;
		}
		
		button.Click = function()
		{
			if ("Switch" == button.type)
			{
				switch (button.status)
				{
					case "Normal":
					button.OnPress(); break;
					case "Pressed":
					button.OnRelease(); break;
				}
			}
			button.OnClick();
		}
		
		button.OnClick = null;
		
		button.iUpdate = function()
		{
			switch (button.status)
			{
				case "Normal":
				if (!normalImg) return;
				if (normalImg.complete)
				{
					contextUI.drawImage(cvsNormalImg, button.bBox.x, button.bBox.y, button.bBox.width, button.bBox.height);
				}
				else
				{
					normalImg.onload = function()
					{
						contextUI.drawImage(cvsNormalImg, button.bBox.x, button.bBox.y, button.bBox.width, button.bBox.height);	
					}
				}
				break;
				
				case "Pressed":
				if (!pressedImg) return;
				if (pressedImg.complete)
				{
					contextUI.drawImage(cvsPressedImg, button.bBox.x, button.bBox.y, button.bBox.width, button.bBox.height);
				}
				else
				{
					pressedImg.onload = function()
					{
						contextUI.drawImage(cvsPressedImg, button.bBox.x, button.bBox.y, button.bBox.width, button.bBox.height);	
					}
				}
				break;
			}
			//button.needValidate = false;
		}
		
		return button;
	}
}

var ProgressBar = 
{
	BuildNew: function(x, y, width, height)
	{
		var progressBar = Control.BuildNew(x, y, width, height);
		progressBar.percentage = 0;
		
		progressBar.SetValue = function(percentage)
		{
			//Round the value.
			progressBar.percentage = (percentage + 0.5) | 0;
			progressBar.needValidate = true;
		}
		
		//Over write the base Update() 'cause this kind of progressBar doesn 't need to clear the original bounding box to update,
		//and so the nearby Othkin logo will not be erased as well.
		progressBar.Update = function()
		{
			if (progressBar.visible) { progressBar.iUpdate(); }
			else progressBar.needValidate = false;
		}
		
		progressBar.iUpdate = function()
		{
			var msg;
			if (100 <= progressBar.percentage) 
			{	
				progressBar.needValidate = false;
				GetTimeInterval(loadDT);
				msg = "已加载100%！";
				setTimeout(function() 
				{ 
					//Log(progressBar.bBox.x + ', ' + progressBar.bBox.y + ', ' + progressBar.bBox.width + ', ' + progressBar.bBox.height);
					progressBar.visible = false; 
					contextUI.clearRect(progressBar.bBox.x, progressBar.bBox.y, progressBar.bBox.width, progressBar.bBox.height); 
					othkinLogoBtn.needValidate = true; 	//I hate this line.
				}, 1200);
			}
			else 
			{
				msg = "已加载" + progressBar.percentage + "%，请稍候…";
			}
			
			var completePixelLength = progressBar.percentage * (progressBar.bBox.width - 2) / 100;
			contextUI.fillStyle = '#939393';
			contextUI.font = '16px Franklin Gothic Medium';
			contextUI.fillText('Loading . . .', progressBar.bBox.x + ((progressBar.bBox.width >> 1) - 30), progressBar.bBox.y + 16);
			contextUI.fillStyle = 'rgba(179, 179, 179, 0.6)';
			contextUI.fillRect(progressBar.bBox.x, progressBar.bBox.y + 20, progressBar.bBox.width, 5);
			contextUI.fillStyle = '#FFFFFF';
			contextUI.fillRect(progressBar.bBox.x + 1 + completePixelLength, progressBar.bBox.y + 21, progressBar.bBox.width - completePixelLength - 2, 3);
			contextUI.fillStyle = 'rgba(152, 204, 243, 0.6)';
			contextUI.fillRect(progressBar.bBox.x + 1, progressBar.bBox.y + 21, completePixelLength, 3);
			
			//progressBar.needValidate = false;
		}
		
		return progressBar;
	}
}

var Slider = 
{
	BuildNew: function(x, y, width, height)
	{
		var slider = Control.BuildNew(x, y, width, height);
		slider.slidable = true;
		
		return slider;
	}
}

var ControlPool = 
{
	BuildNew: function()
	{
		var controlPool = {};
		var children = [];
		var childCount = 0;
		
		controlPool.Add = function(control)
		{
			children[childCount++] = control;
		}
		
		//Note that this function only works in single layer now, that means
		//all controls within must not have intersections.
		controlPool.CheckTarget = function(x, y)
		{
			//Log("Touch down at " + x + ", " + y);
			for (var i = 0; i < childCount; i++)
			{
				if (!children[i].enable || !children[i].visible) continue;
				if (children[i].bBox.x <= x && children[i].bBox.x + children[i].bBox.width > x &&
				children[i].bBox.y <= y && children[i].bBox.y + children[i].bBox.height > y)
				{
					return children[i];
				}
			}
			return null;
		}
		
		controlPool.HandleLoop = function()
		{
			for (var i = 0; i < childCount; i++)
			{
				if (children[i].needValidate)
				{
					children[i].Update();
				}
			}
		}
		
		return controlPool;
	}
};
//UI-----------------------------------------------------------------------------------------'