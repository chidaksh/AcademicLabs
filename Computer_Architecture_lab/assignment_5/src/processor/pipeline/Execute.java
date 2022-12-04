package processor.pipeline;

import processor.Processor;
import java.util.Arrays;

public class Execute {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;

	public Execute(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,
			EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	private static String toBinaryOfSpecificPrecision(int number, int lengthOfTargetString) {
		String binary = String.format("%" + lengthOfTargetString + "s", Integer.toBinaryString(number)).replace(' ',
				'0');
		return binary;
	}

	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
		char[] sign_ext = new char[n];
		Arrays.fill(sign_ext, binary.charAt(0));
		int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
		return signedInteger;
	}

	private void loop(int number) {
		for (int i = 0; i < number; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}

	public void performEX() {
		if (EX_MA_Latch.getisBusy()) {
			OF_EX_Latch.setisBusy(true);
		} else {
			OF_EX_Latch.setisBusy(false);
		}

		loop(30);
		if (OF_EX_Latch.isEX_enable() && !EX_MA_Latch.getisBusy()) {
			int offset = 70000;
			if (OF_EX_Latch.getIsNop()) {
				EX_MA_Latch.setIsNop(true);
				EX_MA_Latch.rd = 75000;
			} else {
				EX_MA_Latch.isNop = false;
				int aluResult = 70000;
				int rs1 = OF_EX_Latch.getOperand1();
				int rs2 = OF_EX_Latch.getOperand2();
				int rd = OF_EX_Latch.getDestinationOperand();
				int imm = OF_EX_Latch.getImm();
				switch (OF_EX_Latch.getOpcode()) {
					case "00000": {
						aluResult = rs1 + rs2;
						break;
					}
					case "00001": {
						aluResult = rs1 + imm;
						break;
					}
					case "00010": {
						aluResult = rs1 - rs2;
						break;
					}
					case "00011": {
						aluResult = rs1 - imm;
						break;
					}
					case "00100": {
						aluResult = rs1 * rs2;
						break;
					}
					case "00101": {
						aluResult = rs1 * imm;
						break;
					}
					case "00110": {
						aluResult = rs1 / rs2;
						int temp = rs1 % rs2;
						containingProcessor.getRegisterFile().setValue(31, temp);
						break;
					}
					case "00111": {
						aluResult = rs1 / imm;
						int temp = rs1 % imm;
						containingProcessor.getRegisterFile().setValue(31, temp);
						break;
					}

					case "01000": {
						aluResult = rs1 & rs2;
						break;
					}
					case "01001": {
						aluResult = rs1 & imm;
						break;
					}
					case "01010": {
						aluResult = rs1 | rs2;
						break;
					}
					case "01011": {
						aluResult = rs1 | imm;
						break;
					}
					case "01100": {
						aluResult = rs1 ^ rs2;
						break;
					}
					case "01101": {
						aluResult = rs1 ^ imm;
						break;
					}

					case "01110": {
						if (rs1 < rs2)
							aluResult = 1;
						else
							aluResult = 0;
						break;
					}
					case "01111": {
						if (rs1 < imm)
							aluResult = 1;
						else
							aluResult = 0;
					}

					case "10000": {
						aluResult = rs1 << rs2;
						String q = Integer.toBinaryString(rs1);
						while (q.length() != 5)
							q = "0" + q;
						String x31 = q.substring(5 - rs2, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31, 2));
						break;
					}
					case "10001": {
						aluResult = rs1 << imm;
						String q = Integer.toBinaryString(imm);

						while (q.length() != 5)
							q = "0" + q;
						String x31 = q.substring(5 - imm, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31, 2));
						break;
					}
					case "10010": {
						aluResult = rs1 >>> rs2;
						String q = Integer.toBinaryString(rs1);

						while (q.length() != 5)
							q = "0" + q;
						String x31 = q.substring(0, rs2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31, 2));
						break;
					}
					case "10011": {
						aluResult = rs1 >>> imm;
						String q = Integer.toBinaryString(imm);

						while (q.length() != 5)
							q = "0" + q;
						String x31 = q.substring(0, imm);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31, 2));
						break;
					}
					case "10100": {
						aluResult = rs1 >> rs2;
						String q = Integer.toBinaryString(rs1);

						while (q.length() != 5)
							q = "0" + q;
						String x31 = q.substring(0, rs2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31, 2));
						break;
					}
					case "10101": {
						aluResult = rs1 >> imm;
						String q = Integer.toBinaryString(imm);

						while (q.length() != 5)
							q = "0" + q;
						String x31 = q.substring(0, imm);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31, 2));
						break;
					}

					case "10110": {
						aluResult = rs1 + imm;
						break;
					}
					case "10111": {
						aluResult = containingProcessor.getRegisterFile().getValue(rd) + imm;
						break;
					}

					case "11000": {
						offset = containingProcessor.getRegisterFile().getValue(rd) + imm;
						break;
					}
					case "11001": {
						if (rs1 == containingProcessor.getRegisterFile().getValue(rd))
							offset = imm;
						break;
					}
					case "11010": {
						if (rs1 != containingProcessor.getRegisterFile().getValue(rd))
							offset = imm;
						break;
					}
					case "11011": {
						if (rs1 < containingProcessor.getRegisterFile().getValue(rd))
							offset = imm;
						break;
					}
					case "11100": {
						if (rs1 > containingProcessor.getRegisterFile().getValue(rd))
							offset = imm;
						break;
					}
					default:
						break;
				}
				if (offset != 70000) {
					EX_IF_Latch.setIsBranchTaken(true);
					EX_IF_Latch.setOffset(offset - 1);
					IF_EnableLatch.setIF_enable(true);
					OF_EX_Latch.setEX_enable(false);
					IF_OF_Latch.setOF_enable(false);
					OF_EX_Latch.setImm(0);
					OF_EX_Latch.setDestinationOperand(0);
					OF_EX_Latch.setOperand1(0);
					OF_EX_Latch.setOperand2(0);
				}
				EX_MA_Latch.setAluResult(aluResult);
				EX_MA_Latch.setOperand1(rs1);
				EX_MA_Latch.setOperand2(rs2);
				EX_MA_Latch.setDestinationOperand(rd);
				EX_MA_Latch.setImm(imm);
				EX_MA_Latch.setOpcode(OF_EX_Latch.getOpcode());
				EX_MA_Latch.setPC(OF_EX_Latch.getPC());

				if (OF_EX_Latch.opcode.equals("11101") == true) {
					OF_EX_Latch.setEX_enable(false);
				}
			}
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);

		}
		// TODO
	}

}
