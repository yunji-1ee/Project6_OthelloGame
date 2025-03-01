import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {

    private JButton[][] button = new JButton[8][8]; // 버튼
    private JLabel[][] labels = new JLabel[8][8]; // 버튼 위에 바둑돌 사진
    private JLabel player1_timerLabel = new JLabel("00");
    private JLabel player2_timerLabel = new JLabel("00");
    private JLabel player1_scoreLabel = new JLabel(" ");
    private JLabel player2_scoreLabel = new JLabel(" ");
    private JLabel player1_winsLabel = new JLabel("0:0"); // 플레이어1의 승리 라운드 레이블
    private JLabel player2_winsLabel = new JLabel("0:0"); // 플레이어2의 승리 라운드 레이블
    private JLabel player1_turnLabel = new JLabel(" "); // 플레이어1의 턴 레이블
    private JLabel player2_turnLabel = new JLabel(" "); // 플레이어2의 턴 레이블
    private char[][] board = new char[8][8]; // 게임 보드 상태를 저장하는 2차원 배열
    private Timer player1_timer;
    private Timer player2_timer;
    private int player1_timeRemaining;
    private int player2_timeRemaining;
    private char currentPlayer = 'B'; // 게임 턴 - 흑인지 백인지
    private int player1_wins = 0; // 플레이어1 승리 수
    private int player2_wins = 0; // 플레이어2 승리 수
    private boolean isPaused = false; // 타이머가 멈춰있는지 여부
    private boolean night = false; // 밤하늘의 별 모드인지의 여부
    private JButton stopButton; // 멈추기 버튼 참조
    private JButton modeButton; // 모드 버튼 참조
    private ImagePanel imagePanel; // 배경 이미지 패널

    // 바둑돌 및 별 이미지 경로
    private final String blackImagePath = "/Users/leeyunji/Desktop/2024_SummerStudy/Project6_OthelloGame/src/Image/black.png";
    private final String whiteImagePath = "/Users/leeyunji/Desktop/2024_SummerStudy/Project6_OthelloGame/src/Image/white.png";
    private final String starImagePath = "/Users/leeyunji/Desktop/2024_SummerStudy/Project6_OthelloGame/src/Image/star.png";
    private final String dayImagePath = "/Users/leeyunji/Desktop/2024_SummerStudy/Project6_OthelloGame/src/Image/Othello.jpg";
    private final String nightImagePath = "/Users/leeyunji/Desktop/2024_SummerStudy/Project6_OthelloGame/src/Image/Othello1.jpg";

    public Main() {

        // 프레임 초기 설정------------------------------------------------------------------------------------
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 배경 이미지 패널 설정
        imagePanel = new ImagePanel();
        imagePanel.setLayout(null);
        imagePanel.setBounds(0, 0, 1000, 750);
        add(imagePanel);

        // 플레이어1 타이머 레이블 설정
        player1_timerLabel.setBounds(90, 348, 100, 35);
        player1_timerLabel.setFont(new Font("Serif", Font.BOLD, 30));
        imagePanel.add(player1_timerLabel);

        // 플레이어2 타이머 레이블 설정
        player2_timerLabel.setBounds(880, 348, 100, 35);
        player2_timerLabel.setFont(new Font("Serif", Font.BOLD, 30));
        imagePanel.add(player2_timerLabel);

        // 플레이어1 점수 레이블 설정
        player1_scoreLabel.setBounds(90, 512, 100, 35);
        player1_scoreLabel.setFont(new Font("Serif", Font.BOLD, 30));
        imagePanel.add(player1_scoreLabel);

        // 플레이어2 점수 레이블 설정
        player2_scoreLabel.setBounds(885, 512, 100, 35);
        player2_scoreLabel.setFont(new Font("Serif", Font.BOLD, 30));
        imagePanel.add(player2_scoreLabel);

        // 플레이어1 승리 라운드 레이블 설정
        player1_winsLabel.setBounds(72, 436, 200, 100);
        player1_winsLabel.setFont(new Font("Serif", Font.PLAIN, 40));
        imagePanel.add(player1_winsLabel);

        // 플레이어2 승리 라운드 레이블 설정
        player2_winsLabel.setBounds(864, 436, 200, 100);
        player2_winsLabel.setFont(new Font("Serif", Font.PLAIN, 40));
        imagePanel.add(player2_winsLabel);

        // 플레이어1 턴 레이블 설정
        player1_turnLabel.setBounds(40, 100, 200, 35);
        player1_turnLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        player1_turnLabel.setForeground(Color.RED);
        imagePanel.add(player1_turnLabel);

        // 플레이어2 턴 레이블 설정
        player2_turnLabel.setBounds(835, 100, 200, 35);
        player2_turnLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        player2_turnLabel.setForeground(Color.RED);
        imagePanel.add(player2_turnLabel);

        // 게임 시작 버튼--------------------------------------------------------------------------------
        JButton gameStart = new JButton("게임시작");
        gameStart.setBounds(845, 20, 100, 35);
        imagePanel.add(gameStart);

        // 도움말 버튼
        JButton helpButton = new JButton("\uD83D\uDCA1도움말");
        helpButton.setBounds(5, 30, 100, 35);
        helpButton.setBorderPainted(false); // 버튼 투명하게 만들기
        helpButton.setContentAreaFilled(false); // 버튼 투명하게 만들기
        helpButton.addActionListener(e -> openHelpLink());
        imagePanel.add(helpButton);

        // 테마 바꾸기 버튼
        modeButton = new JButton("밤하늘");
        modeButton.setBounds(10, 55, 100, 35);
        modeButton.setBorderPainted(false); // 버튼 투명하게 만들기
        modeButton.setContentAreaFilled(false); // 버튼 투명하게 만들기
        modeButton.addActionListener(e -> toggleMode());
        imagePanel.add(modeButton);

        // 멈추기 버튼
        stopButton = new JButton("멈추기");
        stopButton.setBounds(845, 60, 100, 35);
        imagePanel.add(stopButton);

        // 흑 & 백 돌 버튼 설정
        int x = 197, y = 126;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                button[i][j] = new JButton();
                button[i][j].setBounds(x, y, 78, 75);
                button[i][j].setBorderPainted(false); // 버튼 투명하게 만들기
                button[i][j].setContentAreaFilled(false); // 버튼 투명하게 만들기

                // 바둑돌 그림 올릴 레이블
                labels[i][j] = new JLabel();
                labels[i][j].setBounds(x + 10, y + 1, 78, 75);
                imagePanel.add(labels[i][j]);

                // 클릭하는 곳의 인덱스
                int clickX = i;
                int clickY = j;

                // 버튼을 누르면 사진이 올려지도록
                button[i][j].addActionListener(e -> handleButtonClick(clickX, clickY));
                imagePanel.add(button[i][j]);
                button[i][j].setEnabled(false); // 초기 상태에서 버튼 비활성화
                x += 73;
            }
            x = 199;
            y += 70;
        }

        // 게임 시작 버튼
        gameStart.addActionListener(e -> {
            setTimer();
            startGame();
        });

        // 멈추기 버튼
        stopButton.addActionListener(e -> togglePause());

        // 초기 보드 상태 설정
        initializeBoard();
    }

    // 타이머 설정 메소드------------------------------------------------------------------------
    private void setTimer() {
        String time = JOptionPane.showInputDialog(this, "제한 시간을 초 단위로 입력하세요:");
        try {
            int seconds = Integer.parseInt(time);
            player1_timeRemaining = seconds;
            player2_timeRemaining = seconds;
            player1_timerLabel.setText(time);
            player2_timerLabel.setText(time);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "올바른 숫자를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 게임 시작 메소드------------------------------------------------------------------------
    private void startGame() {
        player1_timerLabel.setText(String.valueOf(player1_timeRemaining));
        player2_timerLabel.setText(String.valueOf(player2_timeRemaining));

        if (player1_timer != null) {
            player1_timer.cancel();
        }
        if (player2_timer != null) {
            player2_timer.cancel();
        }

        player1_timer = new Timer();
        player2_timer = new Timer();

        startTimers();

        // 보드 초기화 및 버튼 활성화
        initializeBoard();
        enableValidMoves();
        checkPassTurn(); // 턴 패스 확인
        updateTurnLabel(); // 턴 레이블 업데이트
    }

    private void startTimers() {
        player1_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (currentPlayer == 'B' && !isPaused) {
                        player1_timeRemaining--;
                        player1_timerLabel.setText(String.valueOf(player1_timeRemaining));
                    }

                    if (player1_timeRemaining <= 0) {
                        player1_timer.cancel();
                        player2_timer.cancel();
                        player1_timerLabel.setText("---");
                        announceWinner();
                    }
                });
            }
        }, 1000, 1000);

        player2_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (currentPlayer == 'W' && !isPaused) {
                        player2_timeRemaining--;
                        player2_timerLabel.setText(String.valueOf(player2_timeRemaining));
                    }

                    if (player2_timeRemaining <= 0) {
                        player1_timer.cancel();
                        player2_timer.cancel();
                        player2_timerLabel.setText("---");
                        announceWinner();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void toggleMode() {
        night = !night;
        modeButton.setText(night ? "기본 모드" : "밤하늘");
        imagePanel.setBackgroundImage(night ? nightImagePath : dayImagePath);
        imagePanel.repaint();
    }

    private void togglePause() {
        isPaused = !isPaused;
        stopButton.setText(isPaused ? "다시시작" : "멈추기");
    }

    private void stopGame() { //---------------------------------------------------------------
        if (player1_timer != null) {
            player1_timer.cancel();
        }
        if (player2_timer != null) {
            player2_timer.cancel();
        }
    }

    // 바둑돌 이미지 사이즈 조절하기 (버튼 크기랑 맞춤)
    private ImageIcon resizeIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width - 13, height - 13, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    // 게임의 가장 초기 상태 -> 가운데 흰2 검2
    private void initializeBoard() {
        // 보드 초기화
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
                button[i][j].setEnabled(false); // 초기 상태에서 버튼 비활성화
                labels[i][j].setIcon(null); // 초기 상태에서 별 이미지 제거
            }
        }
        // 초기 돌 4개 가운데 세팅
        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
        currentPlayer = 'B';

        player1_timerLabel.setText(String.valueOf(player1_timeRemaining));
        player2_timerLabel.setText(String.valueOf(player2_timeRemaining));

        playing_baduk(); // 바둑두기
        count_score(); // 초기 점수 계산
        updateTurnLabel(); // 초기 턴 레이블 설정
    }

    // 바둑돌 올리기
    private void playing_baduk() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // 흑색 턴일 때
                if (board[i][j] == 'B') { // 보드 게임 상태가 블랙 턴이면 검정 바둑돌 사진을 해당 인덱스에 올리기
                    labels[i][j].setIcon(resizeIcon(blackImagePath, labels[i][j].getWidth(), labels[i][j].getHeight()));
                } // 흰색 턴일 때
                else if (board[i][j] == 'W') { // 보드 게임 상태가 화이트 턴이면 흰색 바둑돌 사진을 해당 인덱스에 올리기
                    labels[i][j].setIcon(resizeIcon(whiteImagePath, labels[i][j].getWidth(), labels[i][j].getHeight()));
                } else {
                    labels[i][j].setIcon(null); // 빈 공간은 이미지 제거
                }
            }
        }
    }

    private void handleButtonClick(int i, int j) {
        // 가능한 이동인지 확인하고 돌 놓기
        if (isValidMove(i, j, currentPlayer)) {
            placeDisc(i, j, currentPlayer);

            // 현재 플레이어 턴 넘기기
            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
            playing_baduk(); // 바둑두기
            count_score(); // 점수 계산

            enableValidMoves(); // 유효한가?
            checkPassTurn(); // 턴 패스 확인

            // 게임 종료 조건 검사
            if (isGameOver()) {
                announceWinner();
            }
            updateTurnLabel(); // 턴 레이블 업데이트
        }
    }

    private boolean isValidMove(int row, int col, char player) {
        // 가능한 이동인지 확인
        if (board[row][col] != ' ') return false;

        char opponent = (player == 'B') ? 'W' : 'B';

        // 8가지 방향을 체크 (좌, 우, 상, 하, 좌상, 우상, 좌하, 우하)
        int[] dRow = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dCol = {0, 0, -1, 1, -1, 1, -1, 1};

        for (int direction = 0; direction < 8; direction++) {
            int r = row + dRow[direction];
            int c = col + dCol[direction];
            boolean hasOpponentBetween = false;

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c] == opponent) {
                    hasOpponentBetween = true;
                } else if (board[r][c] == player) {
                    if (hasOpponentBetween) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
                r += dRow[direction];
                c += dCol[direction];
            }
        }
        return false;
    }

    // 바둑돌 색 뒤집기------------------------------------------------------------

    private void placeDisc(int row, int col, char player) {
        board[row][col] = player;

        char opponent = (player == 'B') ? 'W' : 'B';

        // 8가지 방향을 체크 (좌, 우, 상, 하, 좌상, 우상, 좌하, 우하)
        int[] dRow = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dCol = {0, 0, -1, 1, -1, 1, -1, 1};

        for (int direction = 0; direction < 8; direction++) {
            int r = row + dRow[direction];
            int c = col + dCol[direction];
            boolean hasOpponentBetween = false;

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c] == opponent) {
                    hasOpponentBetween = true;
                } else if (board[r][c] == player) {
                    if (hasOpponentBetween) {
                        // 사이의 돌을 뒤집기
                        int flipRow = row + dRow[direction];
                        int flipCol = col + dCol[direction];
                        while (flipRow != r || flipCol != c) {
                            board[flipRow][flipCol] = player;
                            flipRow += dRow[direction];
                            flipCol += dCol[direction];
                        }
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
                r += dRow[direction];
                c += dCol[direction];
            }
        }
        count_score(); // 점수 계산
    }

    private void enableValidMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, currentPlayer)) {
                    button[i][j].setEnabled(true);
                    labels[i][j].setIcon(resizeIcon(starImagePath, labels[i][j].getWidth() - 3, labels[i][j].getHeight()));
                } else {
                    button[i][j].setEnabled(false);
                    if (board[i][j] == ' ') {
                        labels[i][j].setIcon(null); // 빈 공간은 이미지 제거
                    }
                }
            }
        }
    }

    private void checkPassTurn() {
        boolean hasValidMove = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, currentPlayer)) {
                    hasValidMove = true;
                    break;
                }
            }
            if (hasValidMove) break;
        }

        if (!hasValidMove) {
            // 다음 플레이어로 턴 넘기기
            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
            enableValidMoves(); // 유효한가?
            // 현재 플레이어도 유효한 이동이 없는 경우, 게임 종료
            if (!hasValidMove) {
                if (!anyValidMoves()) {
                    announceWinner();
                }
            }
            updateTurnLabel(); // 턴 레이블 업데이트
        }
    }

    private boolean anyValidMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, 'B') || isValidMove(i, j, 'W')) {
                    return true;
                }
            }
        }
        return false;
    }

    // 게임 종료 조건----------------------------------------------------------

    private boolean isGameOver() {
        // 이동 가능한 곳이 있는지 확인하여 게임 종료 조건 검사
        if (!anyValidMoves()) {
            return true;
        }
        // 모든 칸이 채워졌는지 확인
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // 결과판 띄우기 --------------------------------------------------------------
    private void announceWinner() {
        // 승자 발표 로직 추가

        if (player1_timeRemaining <= 0) {
            stopGame();
            player2_wins++;
            player1_winsLabel.setText(player1_wins + ":" + player2_wins);
            player2_winsLabel.setText(player2_wins + ":" + player1_wins);
            JOptionPane.showMessageDialog(this, "\uD83D\uDCA5 Game Over \uD83D\uDCA5 \n player2 가 이겼습니다!");
        } else if (player2_timeRemaining <= 0) {
            stopGame();
            player1_wins++;
            player1_winsLabel.setText(player1_wins + ":" + player2_wins);
            player2_winsLabel.setText(player2_wins + ":" + player1_wins);
            JOptionPane.showMessageDialog(this, "\uD83D\uDCA5 Game Over \uD83D\uDCA5 \n player1 이 이겼습니다!");
        } else {
            stopGame();
            int blackCount = 0, whiteCount = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == 'B') {
                        blackCount++;
                    } else if (board[i][j] == 'W') {
                        whiteCount++;
                    }
                }
            }
            String winner;
            if (blackCount > whiteCount) {
                winner = "\uD83C\uDF8A Player1 가 이겼습니다! \uD83C\uDF8A";
                player1_wins++;
            } else if (whiteCount > blackCount) {
                winner = "\uD83C\uDF8A Player2 가 이겼습니다! \uD83C\uDF8A";
                player2_wins++;
            } else {
                winner = "비겼습니다 ..!.! \n 한 번 더 ㄱ ??";
            }
            player1_winsLabel.setText(player1_wins + " : " + player2_wins);
            player2_winsLabel.setText(player2_wins + " : " + player1_wins);
            JOptionPane.showMessageDialog(this, "\uD83D\uDCA5 Game Over \uD83D\uDCA5 \nBlack: " + blackCount + "\nWhite: " + whiteCount + "\n" + winner);
        }
        initializeBoard();
    }

    //각각 점수 세기----------------------------------------------------------------
    private void count_score() {
        int blackCount = 0, whiteCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'B') {
                    blackCount++;
                } else if (board[i][j] == 'W') {
                    whiteCount++;
                }
            }
        }
        player1_scoreLabel.setText(String.valueOf(blackCount)); // 흑돌 개수 업데이트
        player2_scoreLabel.setText(String.valueOf(whiteCount)); // 백돌 개수 업데이트
    }

    // 턴 레이블 업데이트 -----------------------------------------------------------
    private void updateTurnLabel() {
        if (currentPlayer == 'B') {
            player1_turnLabel.setText("흑돌의 순서입니다");
            player2_turnLabel.setText(" ");
        } else {
            player2_turnLabel.setText("흰돌의 순서입니다");
            player1_turnLabel.setText(" ");
        }
    }

    // 도움말 링크 열기 ------------------------------------------------------------
    private void openHelpLink() {
        try {
            Desktop.getDesktop().browse(new java.net.URI("https://youtu.be/6haugpRYGGs?feature=shared"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ImagePanel 클래스 정의
    private class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel() {
            this.backgroundImage = new ImageIcon(dayImagePath).getImage();
        }

        public void setBackgroundImage(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true)); // 이거 안 하면 화면이 꺼져버림
    }
}
