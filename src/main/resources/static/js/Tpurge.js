let message = document.querySelector('.message'),
    message_container = document.querySelector('.message_container'),
    messages = ["You are awesome!","Congrats! You have found the secret message!", "Great job!", "Thanks for playing!", "Secret Message", "Have a nice day!", "May the force be with you!", "Live. Laugh. Love", "Today is the tomorrow you talked about yesterday","Don't Worry. Be Happy!", "Start each day with a grateful heart!","There may be bad days, but there is something good in everyday","When it rains look for rainbows, when it is dark look for stars.", "Enjoy the litte things.", "There is always something to be thankful for.", "Make today awesome!", "Dogs have owners, cats have staff.", "When all else fails take a nap.", "Every expert was once a beginner.", "Failure is the first step to success.", "You are never too old to follow your dreams."];

const getRandomNum = () => {
    return Math.floor(Math.random() * 255);
};

const generateColor = () => {
    return `rgb(${getRandomNum()},${getRandomNum()},${getRandomNum()})`;
};

const setMessageStyles = () => {
    
    message_container.style.opacity = 1;
    message.style.opacity = 1;
    
    document.body.style.setProperty('--message_color', generateColor());
    document.body.style.setProperty('--message_bg_color', generateColor());
    document.body.style.setProperty('--message_shadow_color', generateColor());
};

const setMessage = () => {
    setMessageStyles();
    message.textContent = ` ${messages[Math.floor(Math.random() * messages.length)]} `;
};

setMessage();
setInterval(setMessage, 5000);